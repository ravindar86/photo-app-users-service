package com.client.eureka.photoappusersservice.config;

import com.client.eureka.photoappusersservice.model.dto.UserDto;
import com.client.eureka.photoappusersservice.model.request.UserLoginRequest;
import com.client.eureka.photoappusersservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;

    public AuthenticationFilter(UserService userService, Environment environment) {
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        try {
            UserLoginRequest userLoginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);

            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userLoginRequest.getEmail(),
                                    userLoginRequest.getPassword(),
                                    new ArrayList<>())
                    );

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailsByEmail(userName);

        System.out.println("Time: "+environment.getProperty("token.expiry.time"));
        System.out.println("key: "+environment.getProperty("token.secret.key"));

        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiry.time"))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret.key"))
                .compact();

        response.setHeader("token",token);
        response.setHeader("userId",userDto.getUserId());
    }
}
