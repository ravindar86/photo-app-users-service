package com.client.eureka.photoappusersservice.service;

import com.client.eureka.photoappusersservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserDetailsByEmail(String userName);
}
