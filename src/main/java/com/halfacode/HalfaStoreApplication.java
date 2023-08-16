package com.halfacode;


import com.halfacode.config.JacksonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true) // Add proxy-target-class attribute here
@Import(JacksonConfig.class)
@EnableScheduling
//@OpenAPIDefinition(info = @Info(title = "Library APIS", version = "1.0", description = "E-commerce for halfa-store"))
public class HalfaStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(HalfaStoreApplication.class, args);
	}



}
