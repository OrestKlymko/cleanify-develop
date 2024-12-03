package com.cleaning.cleanify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CleanifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanifyApplication.class, args);
	}

}
