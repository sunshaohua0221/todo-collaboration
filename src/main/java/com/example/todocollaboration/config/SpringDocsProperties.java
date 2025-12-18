package com.example.todocollaboration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "springdocs")
@Setter
@Getter
public class SpringDocsProperties {
    private String title;
    private String description;
    private String version;
    private String header;
    private String scheme;
}
