package com.example.springboot;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RestController
public class SubmissionController {
    @Autowired
    private ApplicationMetrics applicationMetrics;
    private AmazonSNS snsClient;


    @Autowired
    UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
            private AssignmentService assignmentService;
    @Autowired
    private HttpServletRequest request;
    Logger logger= LoggerFactory.getLogger(SubmissionController.class);
    private static final String create_data = "Submitted_the_assignment_url_successfully";
    private static final String create_data_unsuccess = "Unable_to_submit_an_assignment_url";
    private static final String sns_request = "The_Notification_has_been_sent_to_user";
    private static final String sns_request_failed = "The_Notification_has_been_failed_to_send";
    @RequestMapping(value = "/api2/assignments/{id}/submission", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> createAssignment(@RequestBody AssignmentSubmission assignmentSubmission, Principal principal, @PathVariable UUID id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider.builder()
//                .profileName("demo")
//                .build();
       SnsClient snsClient =SnsClient.builder()
                .region(Region.of("us-west-1"))
//                .credentialsProvider(credentialsProvider)
                .build();


        try {

            if (assignmentSubmission == null || auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
                applicationMetrics.addCount(create_data_unsuccess);
                logger.error("Unable to authorize the User or There is no enough Data provided to create the Assignment");
                // throw new UsernameNotFoundException("User not found");
                throw new BadCredentialsException(String.valueOf(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(assignmentSubmission)));
                // return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
            }
            Assignment assignment=assignmentService.getAssignmentById(id.toString());
            if (!LocalDateTime.now().isBefore(Objects.requireNonNull(assignment).getDeadline())) {
                logger.info("Assignment submission deadline has passed");
                applicationMetrics.addCount(create_data_unsuccess);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("{\"You can't submit an assignment\": \"Submission deadline has passed.\"}");
            }

                String loggedId = ((UserDetails) auth.getPrincipal()).getUsername();
                String user_id=userRepository.getUserByFirstName(loggedId).getId();
            int submissioncount= submissionRepository.countByAssignmentIDAndUserId(id.toString(),user_id);
            if( assignment.getNum_of_attempts()<=submissioncount ){
                logger.info("Number of attempts has expired you can not submit an assignment URL");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Number of attempts has expired you can not submit an assignment URL");
            }
            String email = userRepository.getUserByFirstName(loggedId).getEmail();
                String firstName = userRepository.getUserByFirstName(loggedId).getFirstName();
                assignmentSubmission.setUserId(user_id);
                assignmentSubmission.setSubmission_date(LocalDateTime.now());
                assignmentSubmission.setAssignmentID(String.valueOf(id));
                assignmentSubmission.setSubmission_update(LocalDateTime.now());

                submissionRepository.save(assignmentSubmission);


                AmazonSNSClass snsTopicData = new AmazonSNSClass();
                snsTopicData.setSubmissionDate(assignmentSubmission.getSubmission_date().toString());
                snsTopicData.setAssignmentId(assignmentSubmission.getAssignmentID());
                snsTopicData.setSubmissionId(assignmentSubmission.getId().toString());
                snsTopicData.setSubmissionUrl(assignmentSubmission.getSubmission_link().toString());
                snsTopicData.setEmailId(email);
                snsTopicData.setFirstName(firstName);
//                snsTopicData.setAssignmentName();
                ObjectMapper objectMapper = new ObjectMapper();
                String message = "";
                try {
                    message = objectMapper.writeValueAsString(snsTopicData);
                }  catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                try {
                    PublishRequest publishRequest = PublishRequest.builder()
                            .topicArn("arn:aws:sns:us-west-1:609392511080:Assignment_SNS")
                            .message(message)
                            .build();
                    final PublishResponse publishResponse = snsClient.publish(publishRequest);
                    applicationMetrics.addCount(sns_request);
                    logger.info("message has been sent succefully using SNS");



                } catch (Exception e) {
                    logger.error("Could not send an notification");
                    logger.error(e.getMessage());
                    applicationMetrics.addCount(sns_request_failed);

                    return ResponseEntity.status(500).build();

                }
            logger.info("The assignment is created successfully for the User");
            applicationMetrics.addCount(create_data);
            return ResponseEntity.status(HttpStatus.CREATED).build();
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
}
