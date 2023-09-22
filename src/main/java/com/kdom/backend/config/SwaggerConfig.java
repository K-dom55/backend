package com.kdom.backend.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "k-dom API 명세서",
                description = "UNITHON 10th 해커톤",
                version = "v1",
                contact = @Contact(
                        name = "배재은",
                        email = "rew1212@khu.ac.kr"
                )
        )
)
@Configuration
public class SwaggerConfig {
}
