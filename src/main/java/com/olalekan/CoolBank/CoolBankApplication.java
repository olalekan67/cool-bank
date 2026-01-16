package com.olalekan.CoolBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CoolBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoolBankApplication.class, args);
	}

}
