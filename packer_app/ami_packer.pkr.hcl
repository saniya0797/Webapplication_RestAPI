packer {
  required_plugins {
    amazon = {
      source  = "github.com/hashicorp/amazon"
      version = ">= 1.0.0"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-west-1"
}

variable "source_path_jar" {
  type    = string
  default = ""
}
variable "source_ami" {
  type    = string
  default = "ami-071175b60c818694f" # Ubuntu 22.04 LTS
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

variable "subnet_id" {
  type    = string
  default = "subnet-08f2f946f546eb494"
}
variable "aws_demouser" {
  type    = string
  default = "609392511080"
}

variable "aws_devuser" {
  type    = string
  default = "538060352558"
}

# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "my-ami" {
  region          = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_regions = [
    "us-west-1",
  ]
  ami_users = [
    "${var.aws_devuser}",
    "${var.aws_demouser}",
  ]


  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.my-ami"]



  provisioner "file" {
    source      = "users.csv"
    destination = "/tmp/users.csv"
  }
  provisioner "file" {
    #    source      = "../spring-boot/target/spring-boot-0.0.1-SNAPSHOT.jar"
    source      = "${var.source_path_jar}"
    destination = "/tmp/spring-boot-0.0.1-SNAPSHOT.jar"
  }
  provisioner "file" {
    source      = "application.service"
    destination = "/tmp/application.service"
  }
  provisioner "file" {
    source      = "cloudwatch.json"
    destination = "/tmp/cloudwatch.json"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      "sudo apt-get update",
      "sudo apt-get upgrade -y",
      "sudo apt install openjdk-17-jre-headless -y",
      "java -version",
      #      "sudo mkdir /home/assignment",
      #      "cd /home/assignment",
      #      "sudo mv /tmp/users.csv /tmp/spring-boot-0.0.1-SNAPSHOT.jar /home/assignment/",
      "sudo groupadd csye6225",
      "sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225",
      "sudo mv /tmp/users.csv /opt/csye6225/",
      "sudo mv /tmp/spring-boot-0.0.1-SNAPSHOT.jar /opt/csye6225/",
      "sudo mkdir -p /var/log/cloud/",
      "sudo touch /var/log/cloud/csye6225.log",
      "sudo chmod 755 /var/log/cloud/",
      "sudo chmod 664 /var/log/cloud/csye6225.log",
      "sudo chown csye6225:csye6225 /var/log/cloud/",
      "sudo chown csye6225:csye6225 /var/log/cloud/csye6225.log",
      "sudo mv /tmp/application.service /etc/systemd/system/application.service",
      "sudo mv /tmp/cloudwatch.json /var/log/cloudwatch.json",
      "sudo -u csye6225 touch /opt/csye6225/application.properties",
      "sudo chown csye6225:csye6225 /opt/csye6225/spring-boot-0.0.1-SNAPSHOT.jar",
      "sudo chown csye6225:csye6225 /opt/csye6225/application.properties",
      "sudo chmod 750 /opt/csye6225/spring-boot-0.0.1-SNAPSHOT.jar",
      "sudo chown root:root /var/log/cloudwatch.json",
      "sudo chmod 750 /opt/csye6225/application.properties",
      "sudo systemctl enable application.service",
      "sudo wget https://s3.us-west-2.amazonaws.com/amazoncloudwatch-agent-us-west-2/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb",
      "sudo dpkg -i -E ./amazon-cloudwatch-agent.deb",
      "sudo chmod 750 /var/log/cloudwatch.json",
      "sudo systemctl enable amazon-cloudwatch-agent",
      "sudo systemctl start amazon-cloudwatch-agent",
      "sudo apt-get clean",
    ]
  }
}