package com.client.eureka.photoappusersservice.controller;

import com.client.eureka.photoappusersservice.model.dto.UserDto;
import com.client.eureka.photoappusersservice.model.request.UserRequest;
import com.client.eureka.photoappusersservice.model.response.UserResponse;
import com.client.eureka.photoappusersservice.model.response.UserResponseModel;
import com.client.eureka.photoappusersservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        return "User Service Running.."+env.getProperty("local.server.port")+" token -> "+env.getProperty("token.secret.key");
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

    @GetMapping(value="/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);
        UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
