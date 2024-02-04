package dev.sarvesh.userservice.services;

import dev.sarvesh.userservice.dtos.JwtDto;
import dev.sarvesh.userservice.dtos.LoginDto;
import dev.sarvesh.userservice.dtos.SessionStatus;
import dev.sarvesh.userservice.dtos.UserDto;
import dev.sarvesh.userservice.exceptions.NotFoundException;
import dev.sarvesh.userservice.helpers.JwtHelper;
import dev.sarvesh.userservice.models.Session;
import dev.sarvesh.userservice.models.User;
import dev.sarvesh.userservice.populators.UserPopulator;
import dev.sarvesh.userservice.repositories.SessionRepository;
import dev.sarvesh.userservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMapAdapter;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

    private UserRepository userRepository;

    private SessionRepository sessionRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtHelper jwtHelper;

    @Override
    public UserDto createUser(UserDto userDto) {
        final String encodedPassword = bCryptPasswordEncoder.encode(userDto.getEncpass());
        userDto.setEncpass(encodedPassword);
        User savedUser = userRepository.save(UserPopulator.toUser(userDto));
        return UserPopulator.toUserDto(savedUser);
    }

    @Override
    public ResponseEntity<UserDto> login(LoginDto loginDto) {
        assert loginDto.getEmail() != null;
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if(user.isPresent() && bCryptPasswordEncoder.matches(loginDto.getEncpass(), user.get().getEncpass())){
            Session session = new Session();
            session.setUser(user.get());
            byte[] randomBytes = new byte[30];
            new Random().nextBytes(randomBytes);
//            final String token = Base64.getEncoder().encodeToString(randomBytes);
            final String token = jwtHelper.generateJwtToken(user.get());
            session.setToken(token);
            session.setStatus(SessionStatus.ACTIVE);
            sessionRepository.save(session);
            UserDto userDto = UserPopulator.toUserDto(user.get());
            MultiValueMapAdapter<String,String> headers = new MultiValueMapAdapter<>(new HashMap<>());
            headers.add(HttpHeaders.SET_COOKIE,"auth-token:"+token);
            return new ResponseEntity<>(userDto,headers, HttpStatus.OK);

        }
        return null;
    }

    @Override
    @Transactional
    public String logout(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        List<Session> sessions = user.isPresent() ? user.get().getSessions() : Collections.emptyList();
        if(user.isEmpty() || CollectionUtils.isEmpty(sessions)){
            return "No User or logged in session found";
        }

        try{
            sessions.forEach(session -> session.setStatus(SessionStatus.INACTIVE));
            sessionRepository.saveAll(sessions);
        }
        catch(Exception ex){
            throw ex;
        }


        return "User has been successfully logged out";
    }

    @Override
    public UserDto getUser(String userId) {
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if(user.isEmpty()){
            throw new NotFoundException(String.format("coudn't find any user with id - [%s]",userId));
        }
        return UserPopulator.toUserDto(user.get());

    }

    @Override
    public JwtDto decodeJwt(String token,String email) throws AccessDeniedException {
        return jwtHelper.getJwtData(token,email);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserPopulator::toUserDto).toList();
    }
}
