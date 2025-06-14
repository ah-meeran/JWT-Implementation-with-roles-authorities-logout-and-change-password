package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// import com.example.demo.config.JwtConfig;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
// @EnableConfigurationProperties(JwtConfig.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
