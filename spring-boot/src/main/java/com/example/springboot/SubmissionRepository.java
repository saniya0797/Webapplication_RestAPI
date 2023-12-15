package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface SubmissionRepository extends JpaRepository<AssignmentSubmission,String> {
    // List<Assignment> findByEmail(String email);
   List<AssignmentSubmission> findAll();
    void deleteById(String id);
//    AssignmentSubmission getAssignmentById(String id);
    AssignmentSubmission save(AssignmentSubmission assignmentSubmission);
int countByAssignmentIDAndUserId(String id,String user_id);

//



}