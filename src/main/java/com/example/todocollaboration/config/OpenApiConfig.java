package com.example.todocollaboration.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Resource
    private SpringDocsProperties springDocsProperties;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                // 文档描述信息
                .info(new Info()
                        .title(springDocsProperties.getTitle())
                        .description(springDocsProperties.getDescription())
                        .version(springDocsProperties.getVersion())
                )
                // 添加全局的header参数
                .addSecurityItem(new SecurityRequirement()
                        .addList(springDocsProperties.getHeader()))
                .components(new Components()
                        .addSecuritySchemes(springDocsProperties.getHeader(), new SecurityScheme()
                                .name(springDocsProperties.getHeader())
                                .scheme(springDocsProperties.getScheme())
                                .bearerFormat("JWT")
                                .type(SecurityScheme.Type.HTTP))
                );
    }
}
