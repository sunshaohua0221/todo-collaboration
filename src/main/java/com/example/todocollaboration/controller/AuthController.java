package com.example.todocollaboration.controller;

import com.example.todocollaboration.dto.*;
import com.example.todocollaboration.entity.User;
import com.example.todocollaboration.service.UserService;
import com.example.todocollaboration.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "用户接口", description = "用户注册和登录")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "用户注册")
    @PutMapping("/register")
    public ResponseEntity<ApiResponseDto<Boolean>> register(@RequestBody RegisterDto registerDto) {
        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .build();

        // Create user
        userService.register(user);

        ApiResponseDto<Boolean> response = ApiResponseDto.<Boolean>builder()
                .success(true)
                .message("User registered successfully")
                .data(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@RequestBody LoginDto loginDto) {
        try {
            String token = userService.login(loginDto.getUsername(), loginDto.getPassword());
            // Get user details
            User user = userService.getUserByUsername(loginDto.getUsername());

            // Convert User entity to UserDto
            UserDto userDto = convertToUserDto(user);

            // Create auth response
            AuthResponseDto authResponse = AuthResponseDto.builder()
                    .token(token)
                    .user(userDto)
                    .build();

            ApiResponseDto<AuthResponseDto> response = ApiResponseDto.<AuthResponseDto>builder()
                    .success(true)
                    .message("Login successful")
                    .data(authResponse)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            ApiResponseDto<AuthResponseDto> response = ApiResponseDto.<AuthResponseDto>builder()
                    .success(false)
                    .message("Invalid username or password")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
