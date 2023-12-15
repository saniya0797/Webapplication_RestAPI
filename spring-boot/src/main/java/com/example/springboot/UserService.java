package com.example.springboot;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void processCSV(){
        String path="users.csv";
        String csvFilePath=path;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> usersToSave = new ArrayList<>();
        boolean isFirstRow = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath, StandardCharsets.UTF_8));
            String line = "";

            while ((line = reader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip processing first row
                }
               // System.out.println("Processing line: " + line);
                String[] tokens = line.split(",");
if(tokens.length>=4){
                //User user=new User();
                String a = tokens[0];
                String b = (tokens[1]);
                String c = tokens[2];
                String d = tokens[3];
                User existingUser = userRepository.getUserByEmail(c);
                System.out.println("userName: " + existingUser);
                if (existingUser == null) {
                    User user = new User();
                    user.setFirstName(tokens[0]);
                    user.setLast_name(tokens[1]);
                    user.setEmail(tokens[2]);
                    // user.setPassword(tokens[3]);
                    String hashedPassword = passwordEncoder.encode(tokens[3]);
                    user.setPassword(hashedPassword);
                    String generatedId = UUID.randomUUID().toString();
                    user.setId(generatedId);
                    user.setRole("USER");
                    user.setEnabled(true);
                    // user.setId("zfsh");
                    user.setAccount_created(LocalDateTime.now());
                    user.setAccount_updated(LocalDateTime.now());
                    System.out.println("Saving user: " + user);
                    userRepository.save(user);
                }else{
                    System.out.println("Not enough Columns in CSV");
                }
                }
            }
                    // userRepository.saveAll(usersToSave);
                } catch(IOException e){
                    throw new RuntimeException(e);
                }
            }

        }
