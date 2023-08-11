package com.halfacode.dto;

import lombok.Data;

@Data
public class UserRegistrationResponseDto {

    private UserRegistrationDto user;
    private String token;
}
