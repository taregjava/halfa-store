package com.halfacode.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

   // @JsonIgnore // Add this annotation to exclude the 'password' field from JSON serialization
    private String password;
    private AddressDto address;
    private Long roleId; // The selected role ID, set to null if no role is selected

}
