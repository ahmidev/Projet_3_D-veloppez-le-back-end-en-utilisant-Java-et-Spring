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

/**
 * Service for handling rental related operations.
 */
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

    /**
     * Retrieves all rentals.
     *
     * @return a map containing a list of RentalDTO objects
     */
    public Map<String, List<RentalDTO>> getAllRentals() {
        return Map.of("rentals", rentalRepository.findAll().stream()
                .map(mapper::toRentalDTO)
                .toList());
    }

    /**
     * Retrieves a rental by its ID.
     *
     * @param id the ID of the rental
     * @return a ResponseEntity containing the RentalDTO if found, otherwise a 404 response
     */
    public ResponseEntity<RentalDTO> getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toRentalDTO(rental));
    }


    /**
     * Creates a new rental with the given details.
     *
     * @param name the name of the rental
     * @param surface the surface area of the rental
     * @param price the price of the rental
     * @param description the description of the rental
     * @param picture the picture of the rental
     * @param userId the ID of the user creating the rental
     * @throws IOException if an I/O error occurs during file storage
     * @throws IllegalArgumentException if the user ID is null
     * @throws UsernameNotFoundException if the user with the given ID is not found
     */
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

    /**
     * Updates an existing rental with the given details.
     *
     * @param id the ID of the rental to update
     * @param updateRentalDto the data transfer object containing the updated rental details
     * @return a ResponseEntity containing the updated RentalDTO if the rental is found, otherwise a 404 response
     * @throws IOException if an I/O error occurs during the update process
     */
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
