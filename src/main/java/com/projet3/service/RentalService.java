package com.projet3.service;

import com.projet3.dto.CreateRentalDto;
import com.projet3.dto.Mapper;
import com.projet3.dto.RentalDTO;
import com.projet3.dto.UpdateRentalDto;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public Map<String, List<RentalDTO>> getAllRentals() {
        return Map.of("rentals", rentalRepository.findAll().stream()
                .map(mapper::toRentalDTO)
                .toList());
    }

    public ResponseEntity<RentalDTO> getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toRentalDTO(rental));
    }

    public void createRental(String name, Double surface, Double price, String description, MultipartFile picture, Long userId) throws IOException {
        System.out.println("TEST :" + userId);
        String picturePath = "";
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null************");
        }

        if (picture != null && !picture.isEmpty()) {
            picturePath = fileStorageService.storeFile(picture);
            System.out.println("Stored file path: " + picturePath);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        CreateRentalDto createRentalDto = new CreateRentalDto();
        createRentalDto.setName(name);
        createRentalDto.setSurface(surface);
        createRentalDto.setPrice(price);
        createRentalDto.setDescription(description);
        Rental rental = mapper.toRentalEntity(createRentalDto,user,picturePath);
        rental.setUser(user);



        rentalRepository.save(rental);
    }


    public ResponseEntity<RentalDTO> updateRental(Long id, UpdateRentalDto updateRentalDto) throws IOException {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }

        mapper.toRentalEntity(updateRentalDto, rental);
        Rental updatedRental = rentalRepository.save(rental);
        return ResponseEntity.ok(mapper.toRentalDTO(updatedRental));
    }


}
