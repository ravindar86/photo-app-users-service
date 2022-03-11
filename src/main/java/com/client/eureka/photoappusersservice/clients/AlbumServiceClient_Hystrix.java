package com.client.eureka.photoappusersservice.clients;

import com.client.eureka.photoappusersservice.model.response.AlbumResponseModel;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

//@FeignClient(name="ALBUMS-SERVICE", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumServiceClient_Hystrix {

    @GetMapping("/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumServiceClient_Hystrix> {

    @Override
    public AlbumServiceClient_Hystrix create(Throwable cause) {
        return new AlbumServiceFallback(cause);
    }
}

class AlbumServiceFallback implements AlbumServiceClient_Hystrix{

    private Throwable cause;
    private final Logger logger = LoggerFactory.getLogger(AlbumServiceFallback.class);

    public AlbumServiceFallback(final Throwable cause){
        this.cause = cause;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String id){
        if(cause instanceof FeignException && ((FeignException) cause).status()==404) {
            logger.error("404 error => "+cause.getLocalizedMessage());
        }else{
            logger.error("Other error => "+cause.getLocalizedMessage());
        }

        return new ArrayList<>();
    }
}
