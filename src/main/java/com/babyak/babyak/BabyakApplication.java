package com.babyak.babyak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableJpaAuditing
@SpringBootApplication
public class BabyakApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabyakApplication.class, args);
	}

}
