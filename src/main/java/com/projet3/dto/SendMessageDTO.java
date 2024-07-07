package com.projet3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class SendMessageDTO {

    @JsonProperty("rental_id")
    private Long rentalId;

    @JsonProperty("user_id")
    private Long userId;
    private String message;
    private Instant createdAt;
    private Instant updatedAt;


}
