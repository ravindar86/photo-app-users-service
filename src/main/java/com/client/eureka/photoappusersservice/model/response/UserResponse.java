package com.client.eureka.photoappusersservice.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;
}
