package com.projet3.service;

import com.projet3.dto.RentalDTO;
import com.projet3.model.Rental;
import com.projet3.model.User;
import com.projet3.repository.RentalRepository;
import com.projet3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<RentalDTO> getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(rental));
    }

    public void createRental(String name, Double surface, Double price, String description, MultipartFile picture, Long userId) throws IOException {
        System.out.println("TEST :" + userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null************");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setDescription(description);
        rentalDTO.setUserId(userId);
        Rental rental = convertToEntity(rentalDTO);
        rental.setUser(user);

        if (picture != null && !picture.isEmpty()) {
            String picturePath = fileStorageService.storeFile(picture);
            rental.setPicture(picturePath);
        }

        rentalRepository.save(rental);
    }


    public ResponseEntity<RentalDTO> updateRental(Long id, RentalDTO rentalDTO, MultipartFile picture, String email) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }

        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());

        if (picture != null && !picture.isEmpty()) {
            String picturePath = fileStorageService.storeFile(picture);
            rental.setPicture(picturePath);
        }

        Rental updatedRental = rentalRepository.save(rental);
        return ResponseEntity.ok(convertToDTO(updatedRental));
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(rental.getName());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setPicture(rental.getPicture());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setCreatedAt(rental.getCreatedAt());
        rentalDTO.setUpdatedAt(rental.getUpdatedAt());
        rentalDTO.setUserId(rental.getUser().getId());
        return rentalDTO;
    }

    private Rental convertToEntity(RentalDTO rentalDTO) {
        Rental rental = new Rental();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());
        rental.setCreatedAt(rentalDTO.getCreatedAt());
        rental.setUpdatedAt(rentalDTO.getUpdatedAt());
        rental.setUser(userDetailsService.loadUserById(rentalDTO.getUserId()));
        return rental;
    }
}
