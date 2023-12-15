package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AssignmentRepository extends JpaRepository<Assignment,String> {
   // List<Assignment> findByEmail(String email);
    List<Assignment> findAll();
    void deleteById(String id);
Assignment getAssignmentById(String id);
   Assignment save(Assignment assignment);




}
