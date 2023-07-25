package com.halfacode.controller;

import com.halfacode.dto.LoginDto;
import com.halfacode.dto.UserDto;
import com.halfacode.dto.UserRegistrationDto;
import com.halfacode.dto.UserRegistrationResponseDto;
import com.halfacode.entity.User;
import com.halfacode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        // Register the user and get the registration response
        UserRegistrationResponseDto registrationResponse = userService.registerUser(userRegistrationDto);

        // Return the response with HTTP status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> updatedUser = userService.updateUser(id, user);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginRequestDto) {
        String username = loginRequestDto.getUsernameOrEmail();
        String password = loginRequestDto.getPassword();

        // Generate a token for the user
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUsername(username);
        userRegistrationDto.setPassword(password);

        String token = userService.generateToken(userRegistrationDto);

        // Return the token in the response
        return ResponseEntity.ok(token);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}