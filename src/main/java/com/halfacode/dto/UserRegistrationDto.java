package com.halfacode.dto;

import com.halfacode.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {

    private long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Address address;
    private Long roleId; // The selected role ID, set to null if no role is selected

}
