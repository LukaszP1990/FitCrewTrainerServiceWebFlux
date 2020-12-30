package com.fitcrew.trainerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication(scanBasePackages = {"com.fitcrew.trainerservice", "com.fitcrew.jwt"})
@EnableEurekaClient
@EnableReactiveFeignClients
public class TrainerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainerServiceApplication.class, args);
	}

}
