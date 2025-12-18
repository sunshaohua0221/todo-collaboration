package com.example.todocollaboration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.todocollaboration.mapper")
public class TodoCollaborationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoCollaborationApplication.class, args);
    }

}