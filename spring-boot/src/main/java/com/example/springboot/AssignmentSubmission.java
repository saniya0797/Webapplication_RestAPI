package com.example.springboot;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AssignmentSubmission {


    public String getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getSubmission_link() {
        return submission_link;
    }

    public void setSubmission_link(String submission_link) {
        this.submission_link = submission_link;
    }

    public LocalDateTime getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(LocalDateTime submission_date) {
        this.submission_date = submission_date;
    }

    public LocalDateTime getSubmission_update() {
        return submission_update;
    }

    public void setSubmission_update(LocalDateTime submission_update) {
        this.submission_update = submission_update;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid")
    public UUID id;
    @Column
    public String assignmentID;

    @Column
    public String submission_link;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime submission_date;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime submission_update;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column
    private String userId;



}
