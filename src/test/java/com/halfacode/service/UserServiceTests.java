package com.halfacode.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.halfacode.dto.UserDto;
import com.halfacode.dto.UserRegistrationDto;
import com.halfacode.dto.UserRegistrationResponseDto;
import com.halfacode.entity.Role;
import com.halfacode.entity.User;
import com.halfacode.mapper.UserMapper;
import com.halfacode.repoistory.RoleRepository;
import com.halfacode.repoistory.UserRepository;
import com.halfacode.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@DisplayName("UserService Tests")
public class UserServiceTests {

   /* @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateToken_Successful() {
        // Prepare test data
        String username = "testUser";
        String password = "testPassword";

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUsername(username);
        userRegistrationDto.setPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .authorities(Collections.emptyList())
                .build();

        // Mock the behavior of the authentication manager and user details service
        when(authenticationManager.authenticate(any()))
                .thenReturn(null);
        when(customUserDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails);

        // Mock the behavior of the JWT Util and user mapper
        when(userMapper.mapUserDetailsToEntity(userDetails))
                .thenReturn(user);
        when(jwtUtil.generateToken(userRegistrationDto))
                .thenReturn("testToken");

        // Perform the generateToken method and assert the response
        String token = userService.generateToken(userRegistrationDto);

        assertEquals("testToken", token);
    }

    @Test
    public void testGenerateToken_AuthenticationFailure() {
        // Prepare test data
        String username = "testUser";
        String password = "testPassword";

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUsername(username);
        userRegistrationDto.setPassword(password);

        // Mock the behavior of the authentication manager (authentication failure)
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Perform the generateToken method and expect an exception to be thrown
        assertThrows(RuntimeException.class, () -> userService.generateToken(userRegistrationDto));
    }

    @Test
    @DisplayName("Test User Registration Successful")
    public void testRegisterUser_Successful() {
        // Prepare test data
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setName("John Doe");
        registrationDto.setUsername("john.doe");
        registrationDto.setEmail("john.doe@example.com");
        registrationDto.setPassword("password");
        registrationDto.setRoleId(1L); // Assuming roleId is provided during registration

        Role defaultRole = new Role();
        defaultRole.setId(1L);
        defaultRole.setName("ROLE_USER");

        // Mock roleRepository
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(defaultRole));

        // Mock passwordEncoder
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("hashedPassword");

        // Mock userRepository save
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(registrationDto.getName());
        savedUser.setUsername(registrationDto.getUsername());
        savedUser.setEmail(registrationDto.getEmail());
        savedUser.setPassword("hashedPassword");
        savedUser.getRoles().add(defaultRole);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Perform the registerUser method
        UserRegistrationResponseDto responseDto = userService.registerUser(registrationDto);

        // Assertions
        assertNotNull(responseDto);
        assertNotNull(responseDto.getUser());
        assertEquals(registrationDto.getName(), responseDto.getUser().getName());
        assertEquals(registrationDto.getUsername(), responseDto.getUser().getUsername());
        assertEquals(registrationDto.getEmail(), responseDto.getUser().getEmail());
        assertNotNull(responseDto.getToken());
    }

    @Test
    @DisplayName("Test User Registration with Selected Role Successful")
    public void testRegisterUser_SelectedRole_Successful() {
        // Prepare test data with a selected role
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setName("Jane Smith");
        registrationDto.setUsername("jane.smith");
        registrationDto.setEmail("jane.smith@example.com");
        registrationDto.setPassword("password");
        registrationDto.setRoleId(2L); // Role ID of the selected role

        Role selectedRole = new Role();
        selectedRole.setId(2L);
        selectedRole.setName("ROLE_ADMIN");

        // Mock roleRepository
        when(roleRepository.findById(registrationDto.getRoleId())).thenReturn(Optional.of(selectedRole));

        // Mock passwordEncoder
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("hashedPassword");

        // Mock userRepository save
        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setName(registrationDto.getName());
        savedUser.setUsername(registrationDto.getUsername());
        savedUser.setEmail(registrationDto.getEmail());
        savedUser.setPassword("hashedPassword");
        savedUser.getRoles().add(selectedRole);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Perform the registerUser method
        UserRegistrationResponseDto responseDto = userService.registerUser(registrationDto);

        // Assertions
        assertNotNull(responseDto);
        assertNotNull(responseDto.getUser());
        assertEquals(registrationDto.getName(), responseDto.getUser().getName());
        assertEquals(registrationDto.getUsername(), responseDto.getUser().getUsername());
        assertEquals(registrationDto.getEmail(), responseDto.getUser().getEmail());
        assertNotNull(responseDto.getToken());
    }

    @Test
    @DisplayName("Test User Registration with Invalid Username")
    public void testRegisterUser_InvalidUsername() {
        // Prepare test data with an empty username
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setName("Invalid User");
        registrationDto.setUsername("");
        registrationDto.setEmail("invalid.user@example.com");
        registrationDto.setPassword("password");

        // Perform the registerUser method and assert that it throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registrationDto));
    }


    @Test
    public void testUpdateUser_Successful() {
        // Prepare test data
        Long userId = 1L;

        User userToUpdate = new User();
        userToUpdate.setId(userId);
        userToUpdate.setUsername("newUsername");
        userToUpdate.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        // Mock the behavior of the user repository
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(userToUpdate))
                .thenReturn(userToUpdate);

        // Perform the updateUser method and assert the response
        Optional<User> updatedUser = userService.updateUser(userId, userToUpdate);

        assertTrue(updatedUser.isPresent());
        assertEquals(userId, updatedUser.get().getId());
        assertEquals("newUsername", updatedUser.get().getUsername());
        assertEquals("newPassword", updatedUser.get().getPassword());
    }
*/
   /* @Test
    public void testUpdateUser_UserNotFound() {
        // Prepare test data
        Long userId = 1L;

        User userToUpdate = new User();
        userToUpdate.setId(userId);
        userToUpdate.setUsername("newUsername");
        userToUpdate.setPassword("newPassword");

        // Mock the behavior of the user repository (user not found)
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Perform the updateUser method and assert the response
        Optional<User> updatedUser = userService.updateUser(userId, userToUpdate);

        assertFalse(updatedUser.isPresent());
    }

    @Test
    public void testDeleteUser_Successful() {
        // Prepare test data
        Long userId = 1L;

        User userToDelete = new User();
        userToDelete.setId(userId);
        userToDelete.setUsername("testUser");
        userToDelete.setPassword("testPassword");

        // Mock the behavior of the user repository
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userToDelete));

        // Perform the deleteUser method and assert the response
        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);
        verify(userRepository).delete(userToDelete);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Prepare test data
        Long userId = 1L;

        // Mock the behavior of the user repository (user not found)
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Perform the deleteUser method and assert the response
        boolean isDeleted = userService.deleteUser(userId);

        assertFalse(isDeleted);
        verify(userRepository, never()).delete(any());
    }*/

}