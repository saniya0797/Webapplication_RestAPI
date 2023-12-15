package com.example.springboot;

import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;


public class AmazonSNSPub {

    @Value("${sns.topic.arn}")
    private String snsTopicArn; // Replace with your SNS topic ARN

//    private final SnsClient snsClient;
//
//    public SnsPublisher(SnsClient snsClient) {
//        this.snsClient = snsClient;
//    }
//
//    public void publishTableData(String tableName) {
//        String message = "Table Data:\n" + tableName;
//
//        PublishRequest request = PublishRequest.builder()
//                .topicArn(snsTopicArn)
//                .message(message)
//                .build();
//
//        snsClient.publish(request);
//    }

}
