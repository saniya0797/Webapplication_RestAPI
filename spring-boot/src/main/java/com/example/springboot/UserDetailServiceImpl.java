package com.example.springboot;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  HttpServletRequest request;
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(username);
//        HttpSession session = request.getSession();

        if (user == null) {

            throw new UsernameNotFoundException("Could not find user");
        }
//        session.setAttribute("accountId", user.getId());
//        session.setAttribute("email", user.getEmail());
//        session.setAttribute("firstName", user.getFirst_name());
        return new AssignmentDetails(user);
    }



}