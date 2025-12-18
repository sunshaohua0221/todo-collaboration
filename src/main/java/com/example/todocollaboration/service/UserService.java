package com.example.todocollaboration.service;

import com.example.todocollaboration.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    
    User register(User user);
    String login(String username, String password);
    User getUserByUsername(String username);
    UserDetails loadUserByUsername(String username);
}