package com.example.testapi.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestTemplate;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    //@Value("${spring.data.mongodb.username}")
    //private String username;
    //@Value("${spring.data.mongodb.password}")
    //private String password;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private String port;
    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoClient mongoClient() {
        //String uri = String.format("mongodb://%s:%s@%s:%s/%s?ssl=true&retryWrites=false", username, password, host, port, database);
        String uri = String.format("mongodb://%s:%s", host, port);
        return MongoClients.create(uri);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }


    @Bean
    public PushClient pushClient() throws PushClientException {
        return new PushClient();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
