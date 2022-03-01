package com.client.eureka.photoappusersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PhotoAppUsersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppUsersServiceApplication.class, args);
    }

}
