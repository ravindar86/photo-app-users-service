package com.client.eureka.photoappusersservice.service.impl;

import com.client.eureka.photoappusersservice.dto.UserDto;
import com.client.eureka.photoappusersservice.entity.UserEntity;
import com.client.eureka.photoappusersservice.repository.UserRepository;
import com.client.eureka.photoappusersservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    public UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity entity = mapper.map(userDto, UserEntity.class);
        userRepository.save(entity);

        return mapper.map(entity, UserDto.class);
    }
}
