package com.bessatech.ribackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableMongoRepositories("com.bessatech.ribackend.repository.mongo")
@EnableElasticsearchRepositories("com.bessatech.ribackend.repository.elastic")
public class RiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiBackendApplication.class, args);
    }

}
