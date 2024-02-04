package dev.sarvesh.userservice.services;

import dev.sarvesh.userservice.dtos.JwtDto;
import dev.sarvesh.userservice.dtos.LoginDto;
import dev.sarvesh.userservice.dtos.UserDto;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto createUser(UserDto user);

    ResponseEntity<UserDto> login(LoginDto loginDto);

    String logout(String email);

    UserDto getUser(String userId);

    JwtDto decodeJwt(String token,String email) throws AccessDeniedException;

    List<UserDto> getAllUsers();

}
