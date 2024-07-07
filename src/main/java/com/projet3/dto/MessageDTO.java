package com.projet3.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageDTO {

    private Long id;

    private Long rentalId;

    private Long userId;

    private String message;

    private Instant createdAt;

    private Instant updatedAt;


}
