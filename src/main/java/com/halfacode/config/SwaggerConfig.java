package com.halfacode.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "tareg balola",
                        email = "tareg.balola@gmail.com",
                        url = "https://halfacode.com"
                ),
                description = "OpenApi documentation for E-ecommerce Spring boot",
                title = "OpenApi specification -Halfa-Store",
                version = "1.0",
                license = @License(
                        name = "Halfacode",
                        url = "https://halfacode.com"
                ), termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "https://halfacode.com"
                )
        }
)
public class SwaggerConfig {

}
