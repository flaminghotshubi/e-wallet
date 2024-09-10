package com.ewallet.service;

import com.ewallet.model.User;
import com.ewallet.repository.UserCacheRepository;
import com.ewallet.repository.UserRepository;
import com.ewallet.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    UserCacheRepository userCacheRepository;

    public User create(User user) {
        try {
            User newUser = userRepository.save(user);
            kafkaTemplate.send(Constants.USER_CREATED_TOPIC, objectMapper.writeValueAsString(newUser));
            return newUser;
        } catch (Exception e) {
            logger.error("Error occurred while creating user: ", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error occurred while saving user details");
        }
    }

    public User get(int userId) {
        User user = userCacheRepository.get(userId);
        if (null == user) {
            user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
            userCacheRepository.save(user);
        }
        return user;
    }
}
