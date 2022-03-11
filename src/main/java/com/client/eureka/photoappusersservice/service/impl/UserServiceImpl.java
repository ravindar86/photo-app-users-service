package com.client.eureka.photoappusersservice.service.impl;

import com.client.eureka.photoappusersservice.clients.AlbumServiceClient;
import com.client.eureka.photoappusersservice.model.dto.UserDto;
import com.client.eureka.photoappusersservice.model.entity.UserEntity;
import com.client.eureka.photoappusersservice.model.response.AlbumResponseModel;
import com.client.eureka.photoappusersservice.repository.UserRepository;
import com.client.eureka.photoappusersservice.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final AlbumServiceClient feignClient;

    // private RestTemplate restTemplate;
    // private Environment environment;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity entity = mapper.map(userDto, UserEntity.class);
        userRepository.save(entity);

        return mapper.map(entity, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsByEmail(String userName) {
        UserEntity userEntity = userRepository.findByEmail(userName);

        if(userEntity==null) {
            throw new UsernameNotFoundException(userName);
        }
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity==null) {
            throw new UsernameNotFoundException(userId);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

      /*
            // calling album microservice using rest template
            String albumsUrl = String.format(environment.getProperty("albums.url"),userId);

            ResponseEntity<List<AlbumResponseModel>> albumResponse =
                restTemplate.exchange(albumsUrl,
                            HttpMethod.GET,
                            null,
                        new ParameterizedTypeReference<>() {
                        });
             List<AlbumResponseModel> albumsList = albumResponse.getBody();
           */

        // calling album microservice using feign client
       // try {
            List<AlbumResponseModel> albumsList = feignClient.getAlbums(userId);
            userDto.setAlbums(albumsList);
        //} catch(FeignException ex) {
          //  logger.error(ex.getLocalizedMessage());
        //}

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity==null) {
            throw new UsernameNotFoundException(username);
        }
        System.out.println(userEntity.getUserId());
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}
