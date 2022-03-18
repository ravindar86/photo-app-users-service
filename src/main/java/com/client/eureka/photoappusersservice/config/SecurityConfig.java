package com.client.eureka.photoappusersservice.config;

import com.client.eureka.photoappusersservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
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
        http.authorizeRequests()
            //    .antMatchers("/**").hasIpAddress(environment.getProperty("cloud.gateway.server.ip"))
                .antMatchers(HttpMethod.POST, "/users").hasIpAddress(environment.getProperty("cloud.gateway.server.ip"))
                .antMatchers(HttpMethod.GET, "/users").hasIpAddress(environment.getProperty("cloud.gateway.server.ip"))
                .antMatchers(HttpMethod.GET,"/actuator/health").hasIpAddress(environment.getProperty("cloud.gateway.server.ip"))
                .antMatchers(HttpMethod.GET,"/actuator/circuitbreakerevents").hasIpAddress(environment.getProperty("cloud.gateway.server.ip"))
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), environment));

        // to allow accessing h2 console
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, environment);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
        authenticationFilter.setAuthenticationManager(authenticationManager());

        // to handle the login, without the below line it throws 404 error
        authenticationFilter.setAuthenticationSuccessHandler(
                (request, response, authentication) -> response.setStatus(HttpStatus.OK.value()));
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

  /*  @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    } */
}