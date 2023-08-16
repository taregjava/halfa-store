package com.halfacode.mapper;

import com.halfacode.dto.AddressDto;
import com.halfacode.dto.ProductDTO;
import com.halfacode.dto.UserRegistrationDto;
import com.halfacode.entity.Address;
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
               // .password(user.getPassword())
                .address(buildAddressResponseDto(user.getAddress()))
                .roleId(user.getRoles().isEmpty() ? null : user.getRoles().iterator().next().getId())
                .build();
    }
    public User mapDtoToEntity(UserRegistrationDto dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
      //  entity.setPassword(dto.getPassword());
       /* AddressResponseDto addressResponseDto = buildAddressResponseDto(entity.getAddress());
        dto.setAddress(addressResponseDto);*/
        Address addressEntity = new Address();
        addressEntity.setAddressLine1(dto.getAddress().getAddressLine1());
        addressEntity.setAddressLine2(dto.getAddress().getAddressLine2());
        addressEntity.setCity(dto.getAddress().getCity());
        addressEntity.setState(dto.getAddress().getState());
        addressEntity.setPostalCode(dto.getAddress().getPostalCode());
        // Set the converted Address entity in the User entity
        entity.setAddress(addressEntity);

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
        AddressDto addressDto = buildAddressResponseDto(entity.getAddress());
        dto.setAddress(addressDto);

        // Set other fields as needed
        return dto;
    }

    public static AddressDto buildAddressResponseDto(Address address) {
        return AddressDto.builder()
                .addressLine1(address.getAddressLine1())
                .city(address.getCity())
                .state(address.getState())
                .build();
    }
}