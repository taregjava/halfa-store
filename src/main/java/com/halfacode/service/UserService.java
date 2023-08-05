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
import com.halfacode.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       RoleRepository roleRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    public UserPrincipal buildUserPrincipal(User user) {
        // Fetch the authorities for the user (roles)
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // Build the UserPrincipal with the fetched authorities
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .authorities(authorities)
                .build();

        return userPrincipal;
    }

    public String generateToken(UserRegistrationDto userRegistrationDto) {
        try {
            // Extract username and password from the UserRegistrationDto
            String username = userRegistrationDto.getUsername();
            String password = userRegistrationDto.getPassword();

            // Authenticate the user with the provided username and password
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // If authentication is successful, create a UserPrincipal object
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

          /*  Collection<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
*/
            UserPrincipal userPrincipal = buildUserPrincipal(user);

            // Generate a token for the UserPrincipal
            String token = jwtUtil.generateToken(userPrincipal);

            return token;
        } catch (AuthenticationException e) {
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
}
