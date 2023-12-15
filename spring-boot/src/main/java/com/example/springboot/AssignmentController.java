package com.example.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api5/assignments")
class AssignmentController {

    Logger logger= LoggerFactory.getLogger(AssignmentController.class);
    private boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    @Autowired
    private AssignmentRepository assignmentRepository;


    @Autowired
    private ApplicationMetrics applicationMetrics;
    private static final String get_data = "ReadingAllTheDATA_from-Assignment";
    private static final String create_data = "Created_assignment_Successfully";
    private static final String create_data_unsuccess = "Unable_to_create_an_assignment";
    private static final String update_data = "Assignment_updated_successfully";
    private static final String update_data_unsuccess="User_is_not_authorized_to_update_data";
    private static final String delete_data_success = "Assignment_deleted_successfully";
    private static final String delte_data_unsuccess = "Assignment_cant_be_deleted";

    User user = new User();
    @GetMapping
    public List<Assignment> getUsers() {

        System.out.println(" Am I getting Called inside GET  ");
        // Assuming userRepository.findAll() retrieves all users from the database
        List<Assignment> assignment = new ArrayList<>();
        try {
            assignment = assignmentRepository.findAll();
        logger.info("ALl Data is getting retrieved for Reading");
        applicationMetrics.addCount(get_data);
        return assignment;
    } catch (Exception ex) {
            logger.error("Error while fetching assignments: {}", ex.getMessage());
            applicationMetrics.addCount(get_data + "_unsuccess");
            // Handle exceptions and return an appropriate response
            return Collections.emptyList();
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {

            if (assignment == null || auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
                applicationMetrics.addCount(create_data_unsuccess);
                logger.error("Unable to authorize the User or There is no enough Data provided to create the Assignment");
                // throw new UsernameNotFoundException("User not found");
                throw new BadCredentialsException(String.valueOf(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(assignment)));
                // return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
            }

            assignment.setAssignment_created(LocalDateTime.now());
            assignment.setAssignment_updated(LocalDateTime.now());
//            assignment.setId(UUID.randomUUID().toString());
            assignmentRepository.save(assignment);
            logger.info("The assignment is created successfully for the User");
            applicationMetrics.addCount(create_data);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
            //Assignment assign = assignmentRepository.save(assignment);
            //return ResponseEntity.created(URI.create("/api/assignments" + assign.getEmail()))
            //  .body(assign);
        } catch (UsernameNotFoundException ex) {
            // Log a specific message for 401 error
            logger.error("Unauthorized access for post method: {}");
            applicationMetrics.addCount(create_data);
            throw ex;
        }
    }

   @PutMapping("{id}")
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<Assignment> updateAssignment(Authentication authentication,@PathVariable String id, @RequestBody Assignment assignment){
       System.out.println(" Am I getting Called inside Put          ");
Assignment updateAssignment=assignmentRepository.findById(assignment.getId()).orElse(null);
try{
        if (updateAssignment!=null || id!=null || isValidUUID(id)){
            assignment.setId(id);
            assignment.setName(assignment.getName());
            assignment.setDeadline(assignment.getDeadline());
            assignment.setPoints(assignment.getPoints());
            assignment.setNum_of_attempts(assignment.getNum_of_attempts());
            assignment.setAssignment_created(LocalDateTime.now());
            assignment.setAssignment_updated(LocalDateTime.now());
            assignmentRepository.save(assignment);
            applicationMetrics.addCount(update_data);
            logger.info("The Assignemnt is updated successfully");
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();


        }
}catch (ErrorResponseException e){
        applicationMetrics.addCount(update_data_unsuccess);
        logger.error("Update failed!! Unable to authorize the User");
        throw e;
    }
       applicationMetrics.addCount(update_data_unsuccess);
       logger.error("Update failed!! Unable to authorize the User");
       return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }



@DeleteMapping("{id}")
@PreAuthorize("isAuthenticated()")
    public ResponseEntity<Assignment> deleteAssignment(Authentication authentication,@PathVariable String id) {
    System.out.println(" Am I getting Called inside delete          ");
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Assignment assignment=new Assignment();
    try{
    if(assignmentRepository.existsById(id) || isValidUUID(id)){

       assignmentRepository.deleteById(id);
       logger.info("Assignemnt record Deleted Successfully with the authorized user");
        applicationMetrics.addCount(delete_data_success);
      // return ResponseEntity.status(HttpStatus.201).build();
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }}catch (ErrorResponseException er){
        applicationMetrics.addCount(delte_data_unsuccess);
        logger.error("Delete failed!! Unable to authorize the User/ No enough data is provided");
        throw er;

    }
    applicationMetrics.addCount(delte_data_unsuccess);
    logger.error("Delete failed!! Unable to authorize the User/ No enough data is provided");

    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}


}


