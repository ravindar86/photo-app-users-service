package com.client.eureka.photoappusersservice.controller;

import com.client.eureka.photoappusersservice.dto.UserDto;
import com.client.eureka.photoappusersservice.entity.UserEntity;
import com.client.eureka.photoappusersservice.request.UserRequest;
import com.client.eureka.photoappusersservice.response.UserResponse;
import com.client.eureka.photoappusersservice.service.UserService;
import com.client.eureka.photoappusersservice.utils.PasswordEncryption;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private Environment env;
    private BCryptPasswordEncoder pwdEncoder;

    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
        this.pwdEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/status")
    public String statusCheck(){
        System.out.println("Reached port -> "+env.getProperty("local.server.port"));
        return "User Service Running.."+env.getProperty("local.server.port");
    }

    @PostMapping("/status")
    public String postCheck() {
        return "Post check..";
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){

        UserResponse userResponse = null;

        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        userDto.setEncryptedPassword(this.pwdEncoder.encode(userDto.getPassword()));

        UserDto createdUser = userService.createUser(userDto);
        userResponse = mapper.map(createdUser, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
