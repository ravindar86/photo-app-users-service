package com.client.eureka.photoappusersservice;

import feign.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class PhotoAppUsersServiceApplication {

    private final Environment environment;
    private final BeanFactory beanFactory;

    public PhotoAppUsersServiceApplication(BeanFactory beanFactory, Environment environment) {
        this.beanFactory = beanFactory;
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppUsersServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Logger.Level feignLogger(){
        return Logger.Level.FULL;
    }

    @Bean
    public ExecutorService traceableExecutorService() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        return new TraceableExecutorService(beanFactory,executorService);
    }

    @Bean
    @Profile("production")
    public String productionBean(){
        return "Production Bean!! ==> "+environment.getProperty("myapp.env");
    }

    @Bean
    @Profile("default")
    public String defaultBean() {
        return "Default Bean!! ==> "+environment.getProperty("myapp.env");
    }
}
