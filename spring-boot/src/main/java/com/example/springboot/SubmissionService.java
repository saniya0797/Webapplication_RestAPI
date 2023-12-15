package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;

    private Map<String, Integer> requestCounts = new HashMap<>();

    public boolean isRequestLimitExceeded(String userId, int limit) {
        int count = requestCounts.compute(userId, (key, value) -> (value == null) ? 1 : value + 1);
        return count > limit;}}

//    public Assignment createAssignment(AssignmentSubmission assignment) {
//        return submissionRepository.save(assignment);
//    }
//
//    public Assignment updateAssignment(AssignmentSubmission assignment) {
//        return submissionRepository.save(assignment);
//    }
//
//    public void deleteAssignment(String id) {
//        submissionRepository.deleteById(id);
//    }}

