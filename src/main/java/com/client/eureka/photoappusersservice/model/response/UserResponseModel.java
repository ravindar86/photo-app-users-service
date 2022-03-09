package com.client.eureka.photoappusersservice.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseModel {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private List<AlbumResponseModel> albums;

}
