package com.client.eureka.photoappusersservice.clients;

import com.client.eureka.photoappusersservice.model.response.AlbumResponseModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name="ALBUMS-SERVICE")
public interface AlbumServiceClient {

    Logger logger = LoggerFactory.getLogger(AlbumServiceClient.class);

    @GetMapping("/users/{id}/albums")
    @Retry(name="albums-service")
    @CircuitBreaker(name="albums-service", fallbackMethod = "getAlbumsFallback")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);

    default List<AlbumResponseModel> getAlbumsFallback(String id, Throwable cause){
        logger.error(cause.getLocalizedMessage());

        return new ArrayList<>();
    }
}
