package dev.sarvesh.userservice.controllers;

import dev.sarvesh.userservice.dtos.JwtDto;
import dev.sarvesh.userservice.dtos.LoginDto;
import dev.sarvesh.userservice.dtos.SessionStatus;
import dev.sarvesh.userservice.dtos.UserDto;
import dev.sarvesh.userservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestParam final String email){
        return ResponseEntity.ok(userService.logout(email));
    }

    @GetMapping("/validate")
    public ResponseEntity<SessionStatus> validate(@RequestParam final String token){
        return ResponseEntity.ok(SessionStatus.ACTIVE);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto user){
        return ResponseEntity.created(URI.create(user.getName())).body(userService.createUser(user));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<JwtDto> decodeJwt(@RequestParam String token, @RequestParam String email) throws AccessDeniedException {
        return ResponseEntity.ok(userService.decodeJwt(token,email));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAccessDeniedException(Exception ex){

    }


}
