package com.client.eureka.photoappusersservice.model.dto;

import com.client.eureka.photoappusersservice.model.response.AlbumResponseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {

    private static final long serialVersionUID = -3394464686341775746L;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;
    private String encryptedPassword;
    private List<AlbumResponseModel> albums;

}
