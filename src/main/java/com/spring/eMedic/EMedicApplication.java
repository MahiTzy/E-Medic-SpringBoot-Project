package com.spring.eMedic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EMedicApplication {

	public static void main(String[] args) {
		SpringApplication.run(EMedicApplication.class, args);
	}

}
