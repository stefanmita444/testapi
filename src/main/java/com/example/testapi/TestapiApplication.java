package com.example.testapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.TimeZone;

//@EnableSwagger2
@SpringBootApplication
@CrossOrigin(origins = "*")
public class TestapiApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        SpringApplication.run(TestapiApplication.class, args);
    }



}

