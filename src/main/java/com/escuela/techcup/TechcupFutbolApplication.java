package com.escuela.techcup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechcupFutbolApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechcupFutbolApplication.class, args);
	}

}
