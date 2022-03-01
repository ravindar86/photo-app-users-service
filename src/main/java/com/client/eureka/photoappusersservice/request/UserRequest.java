package com.client.eureka.photoappusersservice.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRequest {

    @NotNull
    @Size(min=3, message = "Last Name must be minimum 3 characters")
    private String firstName;

    @NotNull
    @Size(min=3, message = "Last Name must be minimum 3 characters")
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min=5, max=12, message = "Password length must be between 5 and 12 characters")
    private String password;

}
