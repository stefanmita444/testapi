package com.example.testapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class TestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestapiApplication.class, args);
	}

}
