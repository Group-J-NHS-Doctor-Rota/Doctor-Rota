package edu.uob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// Disable autoconfiguration of security (importantly the spring login)
// https://stackoverflow.com/questions/46265775/spring-boot-project-shows-the-login-page
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MySpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringApplication.class, args);
	}
}
