package dev.sarvesh.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurity {

    // it creates a bean of securityfilterchain, so whenever a SecurityFilterChain object is needed
    // it will come from here.
//    @Bean
//    public SecurityFilterChain permitAll(HttpSecurity http) throws Exception {
//        // this removes auth from all endpoints
//        // csrf is disabled as spring security by default wont
//        return http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()).csrf().disable().build();
//
//    }

    // this will create a bean to be injected wherever bcryptpasswordencoder is used
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
