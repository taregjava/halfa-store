package com.halfacode.service;


import com.halfacode.dto.UserDto;
import com.halfacode.dto.UserRegistrationDto;
import com.halfacode.dto.UserRegistrationResponseDto;
import com.halfacode.entity.Role;
import com.halfacode.entity.User;
import com.halfacode.mapper.UserMapper;
import com.halfacode.repoistory.RoleRepository;
import com.halfacode.repoistory.UserRepository;
import com.halfacode.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public String generateToken(UserRegistrationDto userRegistrationDto) {
        try {
            // Extract username and password from the UserRegistrationDto
            String username = userRegistrationDto.getUsername();
            String password = userRegistrationDto.getPassword();

            // Authenticate the user with the provided username and password
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // If authentication is successful, generate a token for the user
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            User user = userMapper.mapUserDetailsToEntity(userDetails);
            return jwtUtil.generateToken(userRegistrationDto);
        } catch (AuthenticationException e) {
            // If authentication fails, handle the exception accordingly (e.g., throw custom exception)
            throw new RuntimeException("Invalid username or password");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserRegistrationResponseDto registerUser(UserRegistrationDto userRegistrationDto) {
        // Use the UserMapper to convert UserRegistrationDto to User entity
        User user = userMapper.mapDtoToEntity(userRegistrationDto);

        // If the user selected a role during registration, add it to the roles set
        if (userRegistrationDto.getRoleId() != null) {
            Role selectedRole = new Role();
            selectedRole.setId(userRegistrationDto.getRoleId());
            // Since CascadeType.PERSIST is specified, only new roles will be persisted
            user.getRoles().add(selectedRole);
        } else {
            // If no role is selected, set a default role for regular users
            Optional<Role> defaultRoleOptional = roleRepository.findByName("ROLE_USER");
            Role defaultRole = defaultRoleOptional.orElseGet(() -> {
                // If the default role does not exist, create it and save it to the database
                Role newDefaultRole = new Role();
                newDefaultRole.setName("ROLE_USER");
                return roleRepository.save(newDefaultRole);
            });
            user.getRoles().add(defaultRole);
        }

        // Ensure the user has a valid username before generating the token
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User must have a valid username");
        }

        String hashedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        user.setPassword(hashedPassword);
        // Save the user to the database
        user = userRepository.save(user);

        // Generate a token for the user
        String token = jwtUtil.generateToken(userRegistrationDto);

// Create the UserRegistrationResponseDto and set the token
        UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto();
        responseDto.setUser(userMapper.mapEntityToDto(user));
        responseDto.setToken(token);

        return responseDto;
    }


    public Optional<User> updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userRepository.save(user);
            return Optional.of(updatedUser);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        } else {
            return false;
        }
    }
  /*  public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }*/
}
