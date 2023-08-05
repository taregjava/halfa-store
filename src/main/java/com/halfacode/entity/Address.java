package com.halfacode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address_line_1", nullable = false, length = 64)
    protected String addressLine1;

    @Column(name = "address_line_2", length = 64)
    protected String addressLine2;

    @Column(nullable = false, length = 45)
    protected String city;

    @Column(nullable = false, length = 45)
    protected String state;

    @Column(name = "postal_code", nullable = false, length = 10)
    protected String postalCode;
}
