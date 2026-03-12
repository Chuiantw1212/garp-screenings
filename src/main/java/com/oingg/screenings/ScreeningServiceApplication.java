package com.oingg.screenings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableCaching
public class ScreeningServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreeningServiceApplication.class, args);
	}

}
