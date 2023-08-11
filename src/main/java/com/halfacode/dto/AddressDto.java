package com.halfacode.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private String addressLine1;
    @JsonIgnore
    protected String addressLine2;
    private String city;
    private String state;
  //  @JsonIgnore
    protected String postalCode;
}