package com.halfacode.mapper;

import com.halfacode.dto.ProductDTO;
import com.halfacode.dto.UserRegistrationDto;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public static UserRegistrationDto buildUserDTO(User user) {
        return UserRegistrationDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .address(user.getAddress())
                .roleId(user.getRoles().isEmpty() ? null : user.getRoles().iterator().next().getId())
                .build();
    }
    public User mapDtoToEntity(UserRegistrationDto dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setAddress(dto.getAddress());
        return entity;
    }
    public User mapUserDetailsToEntity(UserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());

        // If you have other fields in the User entity that you want to map from UserDetails,
        // you can do so here. For example:
        // user.setName(userDetails.getFirstName() + " " + userDetails.getLastName());

        // Note that the UserDetails interface may not have all the fields you need in your User entity,
        // so make sure to map only the available fields or adjust the UserDetails implementation accordingly.

        // Add other mappings as needed
        return user;
    }
    public UserRegistrationDto mapEntityToDto(User entity) {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName()); // Change this line to set the correct field name
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setAddress(entity.getAddress());
        // Set other fields as needed
        return dto;
    }
}