package com.example.todocollaboration.service.impl;

import com.example.todocollaboration.entity.User;
import com.example.todocollaboration.mapper.UserMapper;
import com.example.todocollaboration.service.UserService;
import com.example.todocollaboration.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public User register(User user) {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        
        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = this.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails.getUsername());
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                 user.getUsername(),
                 user.getPassword(), // 这里应该是加密后的密码
                 new ArrayList<>() // 这里应加载用户的权限/角色列表
         );
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}