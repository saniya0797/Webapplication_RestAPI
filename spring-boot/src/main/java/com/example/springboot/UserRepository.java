package com.example.springboot;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, String> {

   public User getUserByEmail(String email);
   User getAssignmentById(String id);
   User getUserByFirstName(String first_name);
   User save(User user);

}
