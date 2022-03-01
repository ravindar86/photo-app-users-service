package com.client.eureka.photoappusersservice.config;

import com.client.eureka.photoappusersservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(Environment environment, UserService userService){
        this.environment = environment;
        this.userService = userService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //http.authorizeRequests().antMatchers("/users/**").permitAll();

        // to allow access the resources only via cloud gateway server
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress(environment.getProperty("cloud.gateway.server.ip"))
                .and()
                .addFilter(getAuthenticationFilter());

        // to allow accessing h2 console
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, environment);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
        authenticationFilter.setAuthenticationManager(authenticationManager());

        // to handle the login
        authenticationFilter.setAuthenticationSuccessHandler(
                (request, response, authentication) -> response.setStatus(HttpStatus.OK.value()));
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}