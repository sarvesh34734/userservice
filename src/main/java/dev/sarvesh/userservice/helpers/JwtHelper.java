package dev.sarvesh.userservice.helpers;

import dev.sarvesh.userservice.dtos.JwtDto;
import dev.sarvesh.userservice.exceptions.NotFoundException;
import dev.sarvesh.userservice.models.Session;
import dev.sarvesh.userservice.models.User;
import dev.sarvesh.userservice.repositories.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtHelper {

    private static final MacAlgorithm alg = Jwts.SIG.HS256;
    private static final SecretKey key = alg.key().build();

    private SessionRepository sessionRepository;

    public String generateJwtToken(User user){

        Map<String,Object> jsonClaim = new HashMap<>();
        jsonClaim.put("userId",user.getId());
        jsonClaim.put("email",user.getEmail());
        jsonClaim.put("roles", List.of("MENTOR","STUDENT"));
        jsonClaim.put("expiry", Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));

        return Jwts.builder()
                .claims(jsonClaim)
                .signWith(key,alg)
                .compact();
    }

    public JwtDto getJwtData(String token,String userEmail) throws AccessDeniedException {
        Jws<Claims> claims;
        try{
            claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new AccessDeniedException("Invalid Token");
        }

        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token,userEmail);
        if(session.isEmpty()){
            throw new NotFoundException("no session found for the user");
        }

        String userId = (String) claims.getPayload().get("userId");
        String email = (String) claims.getPayload().get("email");
        List<String> roles = (List<String>) claims.getPayload().get("roles");
        Long expiry = (Long) claims.getPayload().get("expiry");

        return JwtDto.builder().email(email).roles(roles).expiry(expiry).userId(userId).build();
    }

}
