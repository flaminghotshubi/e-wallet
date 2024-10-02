package com.ewallet.controller;

import com.ewallet.dto.CreateUserRequest;
import com.ewallet.model.User;
import com.ewallet.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest.to());
    }

    @GetMapping("")
    public Object getUser(@RequestParam("userId") int id) {
        return userService.get(id);
    }
}
