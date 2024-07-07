package com.projet3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class SendMessageDTO {


    @JsonProperty("rental_id")
    private Long rentalId;


    @JsonProperty("user_id")
    private Long userId;

    @NotBlank
    private String message;

    private Instant createdAt;

    private Instant updatedAt;


}
