package com.example.springboot;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.sql.DriverManager;

@RestController
@RequestMapping("healthz")
public class HealthCheck {

    @Autowired
    ApplicationMetrics applicationMetrics;
    Logger logger= LoggerFactory.getLogger(HealthCheck.class);

    private static String healthz_successful = "helathz-is-working(200 ok)";
    private static String healthz_unsuccessful = "healthz-endpoint-not-working(500 unavaialble)";
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
    @GetMapping
    public ResponseEntity<Void> checkHealth() {
        boolean isDatabaseConnected = checkDatabaseConnection();
        if (isDatabaseConnected) {
          applicationMetrics.addCount(healthz_successful);
          logger.info("HealthCheck of our API is working and retuning 200 ok");
            return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(null);

        } else {
           applicationMetrics.addCount(healthz_unsuccessful);
            logger.error("HealthCheck of our API Dead, Service not available");

            return ResponseEntity.status(503).cacheControl(CacheControl.noCache()).contentLength(0).body(null);


        }
    }

    public ResponseEntity<Void> checkOtherMethods() {
        logger.error("End point does not exist");
        return ResponseEntity.status(405).cacheControl(CacheControl.noCache()).contentLength(0).body(null);
    }

    private boolean checkDatabaseConnection() {
        try (Connection conn= DriverManager.getConnection("jdbc:mariadb://"+dbHost+":"+dbPort+"/", dbUser, password)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
