package com.achiever.menschenfahren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(MenschenFahrenApplication.PROPERTY_FILE)
public class MenschenFahrenApplication {

	public static final String PROPERTY_FILE = "classpath:application.properties";

	public static void main(String[] args) {
		SpringApplication.run(MenschenFahrenApplication.class, args);
	}

}
