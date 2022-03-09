package com.client.eureka.photoappusersservice.clients;

import com.client.eureka.photoappusersservice.model.response.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="ALBUMS-SERVICE")
public interface AlbumServiceClient {

    @GetMapping("/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}
