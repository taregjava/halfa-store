package com.halfacode.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.halfacode.dto.UserRegistrationDto;
import com.halfacode.dto.UserRegistrationResponseDto;
import com.halfacode.entity.Role;
import com.halfacode.entity.User;
import com.halfacode.mapper.UserMapper;
import com.halfacode.repoistory.RoleRepository;
import com.halfacode.repoistory.UserRepository;
import com.halfacode.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private OTPService otpService; // Inject the OTPService

    public UserRegistrationResponseDto registerUser(UserRegistrationDto userRegistrationDto) throws NumberParseException {
        User user = userMapper.mapDtoToEntity(userRegistrationDto);

        // Hash password and save user
        String hashedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        user.setPassword(hashedPassword);
        user = userRepository.save(user);

        // Generate a token for the user
        String token = generateToken(userRegistrationDto);
        // Generate and send OTP to the user's phone number
        String phoneNumber = userRegistrationDto.getPhoneNumber();
        String otp = otpService.generateAndSendOTP(phoneNumber);

        // Create the UserRegistrationResponseDto and set the token
        UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto();
        responseDto.setUser(userMapper.mapEntityToDto(user));
        responseDto.setToken(token);

        return responseDto;
    }

    public String generateToken(UserRegistrationDto userRegistrationDto) {
        // Generate and return token without authenticating
        return jwtUtil.generateToken(userRegistrationDto);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

  /*  public UserRegistrationResponseDto registerUser(UserRegistrationDto userRegistrationDto) {
        User user = userMapper.mapDtoToEntity(userRegistrationDto);

        // If the user selected a role during registration, add it to the roles set
        if (userRegistrationDto.getRoleId() != null) {
            Role selectedRole = new Role();
            selectedRole.setId(userRegistrationDto.getRoleId());
            user.getRoles().add(selectedRole);
        } else {
            // If no role is selected, set a default role for regular users
            Optional<Role> defaultRoleOptional = roleRepository.findByName("ROLE_USER");
            Role defaultRole = defaultRoleOptional.orElseGet(() -> {
                Role newDefaultRole = new Role();
                newDefaultRole.setName("ROLE_USER");
                return roleRepository.save(newDefaultRole);
            });

            if (defaultRole != null) {
                user.getRoles().add(defaultRole);
            } else {
                throw new IllegalStateException("Default role is null");
            }
        }

        // Ensure the user has a valid username before generating the token
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User must have a valid username");
        }

        String hashedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        user.setPassword(hashedPassword);
        user = userRepository.save(user);

        // Generate a token for the user
        String token = generateToken(userRegistrationDto);

        // Create the UserRegistrationResponseDto and set the token
        UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto();
        responseDto.setUser(userMapper.mapEntityToDto(user));
        responseDto.setToken(token);

        return responseDto;
    }*/

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
}
