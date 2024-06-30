package com.projet3.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private Long id;


    private String email;


    private String name;

    private Instant createdAt;
    private Instant updatedAt;

    // getters and setters
}
