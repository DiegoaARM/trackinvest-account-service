package com.trackinvest.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrackInvestAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackInvestAccountServiceApplication.class, args);
	}

}
