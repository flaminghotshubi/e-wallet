package com.ewallet.controller;

import com.ewallet.dto.CreateUserRequest;
import com.ewallet.dto.LoginRequest;
import com.ewallet.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String welcome() {
        return "Hi!";
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest.to());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest request) {
        return userService.authenticateUser(request);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam("token") @NotBlank String token) {
        return userService.validateToken(token);
    }

    @GetMapping("")
    public Object getUser() {
        return userService.getUserDetails();
    }
}
