package com.halfacode.dto;

import com.halfacode.entity.Address;
import lombok.Data;

@Data
public class OrderCreateRequest {
    private Long userId;
    private Address address;

}
