package com.projet3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateRentalDto {

    @NotBlank
    private String name;
    @NotNull
    private Double surface;
    @NotNull
    private Double price;
    @NotBlank
    private MultipartFile picture;
    @NotBlank
    private String description;


}
