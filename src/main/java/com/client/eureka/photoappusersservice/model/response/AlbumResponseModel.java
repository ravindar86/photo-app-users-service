package com.client.eureka.photoappusersservice.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumResponseModel {

    private String albumId;
    private String userId;
    private String name;
    private String description;
}
