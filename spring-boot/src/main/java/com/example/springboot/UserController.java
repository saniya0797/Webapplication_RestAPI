package com.example.springboot;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Value("${DB_PORT}")
    String dbPort;

    @Value("${DB_HOST}")
    String dbHost;


    @Value("${DB_USER}")
    String dbUser;

    @Value("${DB_PASSWORD}")
    String password;
    @Value("${DB_NAME}")
    String dbname;
    //    @GetMapping
    public ResponseEntity<Void> checkHealth() {
        boolean isDatabaseConnected = checkDatabaseConnection();
//
       if (isDatabaseConnected) {
            return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(null);
       } else {
            return ResponseEntity.status(503).cacheControl(CacheControl.noCache()).contentLength(0).body(null);
       }
    }
//
//
//    public ResponseEntity<Void> checkOtherMethods() {
//        return ResponseEntity.status(405).cacheControl(CacheControl.noCache()).contentLength(0).body(null);
//    }
//
    private boolean checkDatabaseConnection() {
        try (Connection conn= DriverManager.getConnection("jdbc:mariadb://dbHost:dbPort/dbname", "dbUser", "password")) {
            return true;
       } catch (SQLException e) {
           return false;
        }
    }
//
//
//    private final UserDTO userDTO;

//    @PostMapping("/uploadCSV")
//     public ResponseEntity<String> uploadCSV(@RequestParam("") MultipartFile file) {
//        System.out.println("1");
//
//        us.processCSV();
//        return ResponseEntity.ok("CSV data processed and saved.");
//    }

    //    public EntityController(UserDTO userDTO) {
//        this.userDTO = userDTO;
//    }
    public ResponseEntity<Void> checkOtherMethods() {
        return ResponseEntity.status(405).cacheControl(CacheControl.noCache()).contentLength(0).body(null);
    }
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getUsers(Authentication authentication) {
        // Assuming userRepository.findAll() retrieves all users from the database
        List<User> users = new ArrayList<>();

            users = userRepository.findAll();


        return users;
    }

}



