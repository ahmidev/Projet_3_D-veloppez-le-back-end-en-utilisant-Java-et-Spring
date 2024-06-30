package com.projet3.controller;

import com.projet3.dto.RentalDTO;
import com.projet3.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public List<RentalDTO> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture,
            @PathVariable("userId") Long userId) throws IOException {
        rentalService.createRental(name, surface, price, description, picture, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental created !");
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(@PathVariable Long id,
                                                  @RequestPart("rental") @Valid RentalDTO rentalDTO,
                                                  @RequestPart("picture") MultipartFile picture) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String currentPrincipalName = authentication.getName();
        return rentalService.updateRental(id, rentalDTO, picture, currentPrincipalName);
    }
}
