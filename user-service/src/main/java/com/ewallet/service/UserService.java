package com.ewallet.service;

import com.ewallet.dto.UserDetailsObject;
import com.ewallet.dto.LoginRequest;
import com.ewallet.dto.LoginResponse;
import com.ewallet.model.User;
import com.ewallet.repository.UserRepository;
import com.ewallet.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    public ResponseEntity<?> create(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User newUser = userRepository.save(user);
            kafkaTemplate.send(Constants.USER_CREATED_TOPIC, objectMapper.writeValueAsString(newUser));
            return new ResponseEntity<>(getUserObject(newUser), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating user: ", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error occurred while saving user details");
        }
    }

    public ResponseEntity<?> authenticateUser(LoginRequest request) {
        Authentication authentication = null;
        try {
            authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Bad credentials");
            body.put("status", false);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateTokenFromUsername(request.getUsername());
        LoginResponse response = LoginResponse.builder()
                .username(request.getUsername())
                .jwtToken(jwtToken)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> validateToken(String token) {
        if (jwtService.validateJwtToken(token))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getUserDetails() {
        Authentication authObject = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authObject.getPrincipal();
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return new ResponseEntity<>(getUserObject(user), HttpStatus.OK);
    }

    private UserDetailsObject getUserObject(User user) {
        return UserDetailsObject.builder()
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

    /*
    if implementing using cache:
    //        User user = userCacheRepository.get(userId);
//        if (null == user) {
//            user = userRepository
//                    .findById(userId)
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
//            userCacheRepository.save(user);
//        }
//        return user;
     */
}
