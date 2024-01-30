package com.seahield.hostserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HostserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostserverApplication.class, args);
	}

}
