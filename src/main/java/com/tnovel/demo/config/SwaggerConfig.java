package com.tnovel.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    @Value("${springdoc.swagger-ui.config-url}")
    private String url;

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(new Info()
                        .version("v1")
                        .title("나만의 여행 이야기, Tnovel")
                        .description("유저와 게시판을 JPA 엔티티로 연결한 REST API")
                )
                .addSecurityItem(securityRequirement)
                .components(components)
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("로컬 서버"),
                        new Server().url(url).description("배포 서버")
                ));
    }

    @Bean
    public GroupedOpenApi grouping() {
        String[] paths = {"/**"};
        return GroupedOpenApi.builder()
                .group("spec")
                .pathsToMatch(paths)
                .build();
    }
}
