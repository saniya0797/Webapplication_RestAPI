package com.example.springboot;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.ReadablePartial;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Component

@Entity
@Table(name="assignment")
public class Assignment {
    @Id
    @Column
    public String id;

    @Column
    public String name;
    @Column
    public Integer points;
    @Column
    public  Integer num_of_attempts;

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//   @OneToMany
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//   private User user;
@OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
@JoinColumn(name="id_user",referencedColumnName = "id")
private List<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getNum_of_attempts() {
        return num_of_attempts;
    }

    public void setNum_of_attempts(Integer num_of_attempts) {
        this.num_of_attempts = num_of_attempts;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getAssignment_created() {
        return assignment_created;
    }

    public void setAssignment_created(LocalDateTime assignment_created) {
        this.assignment_created = assignment_created;
    }

    public LocalDateTime getAssignment_updated() {
        return assignment_updated;
    }

    public void setAssignment_updated(LocalDateTime assignment_updated) {
        this.assignment_updated = assignment_updated;
    }
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime deadline;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime  assignment_created;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime assignment_updated;
}
