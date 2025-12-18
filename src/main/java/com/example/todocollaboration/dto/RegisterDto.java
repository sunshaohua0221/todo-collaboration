package com.example.todocollaboration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "注册DTO")
public class RegisterDto {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "email")
    private String email;
    @Schema(description = "密码")
    private String password;
}