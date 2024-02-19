package dev.sarvesh.userservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sarvesh.userservice.clients.KafkaProducerConfig;
import dev.sarvesh.userservice.dtos.JwtDto;
import dev.sarvesh.userservice.dtos.LoginDto;
import dev.sarvesh.userservice.dtos.SendEmailDto;
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

    private static final String htmlContent = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Welcome to Your Company/Service Name!</title>\n" +
            "</head>\n" +
            "<body style=\"font-family: Arial, sans-serif;\">\n" +
            "\n" +
            "    <table style=\"max-width: 600px; margin: 0 auto; padding: 20px; border-collapse: collapse; border: 1px solid #ccc;\">\n" +
            "        <tr>\n" +
            "            <td style=\"text-align: center; background-color: #f8f8f8; padding: 10px;\">\n" +
            "                <h1>Welcome to Kafka Notification Service!</h1>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td style=\"padding: 20px;\">\n" +
            "                <p>Hey there! Glad to see you here.</p>\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "    </table>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    private UserService userService;

    private KafkaProducerConfig kafkaProducerConfig;

    private ObjectMapper objectMapper;

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
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto user) throws JsonProcessingException {
        SendEmailDto dto = new SendEmailDto();
        dto.setTo(user.getEmail());
        dto.setFrom("sarvesh.vyas.2096@gmail.com");
        dto.setSubject("Welcome to the kafka tutorial");
        dto.setBody(htmlContent);
        kafkaProducerConfig.send("sendEmail",objectMapper.writeValueAsString(dto));
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
