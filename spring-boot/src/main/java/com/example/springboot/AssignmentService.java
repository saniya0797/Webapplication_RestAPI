package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AssignmentService {
    User user;
    @Autowired
     AssignmentRepository assignmentRepository;


    public Assignment createAssignment(Assignment assignment) {
        if (assignment.getPoints() < 1 || assignment.getPoints() > 10) {
            throw new IllegalArgumentException("Points must be between 1 and 10.");
        }

        assignment.setAssignment_created(LocalDateTime.now());
        assignment.setAssignment_updated(LocalDateTime.now());

        return assignmentRepository.save(assignment);
    }

//    public Assignment createAssignment(Assignment assignment) {
//        assignment.setAssignment_created(LocalDateTime.now());
//        return assignmentRepository.save(assignment);
//    }

    public Assignment updateAssignment(Assignment assignment) {
        assignment.setAssignment_updated(LocalDateTime.now());
        return assignmentRepository.save(assignment);
    }


    public  void deleteAssignment(String id) {
        assignmentRepository.deleteById(id);
    }
//    public Assignment getAssignmentById(String id) {
//        return assignmentRepository.findById(id).orElse(null);
//    }


    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    public  Assignment getAssignmentById(String id){
        return  assignmentRepository.getAssignmentById(id);

    }



}
