package com.projet3.service;

import com.projet3.dto.Mapper;
import com.projet3.dto.SendMessageDTO;
import com.projet3.model.Message;
import com.projet3.model.Rental;
import com.projet3.model.User;
import com.projet3.repository.MessageRepository;
import com.projet3.repository.RentalRepository;
import com.projet3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for handling message related operations.
 */
@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private MessageRepository messageRepository;


    /**
     * Sends a message and saves it to the database.
     *
     * @param sendMessageDTO the data transfer object containing the message details
     * @return a SendMessageDTO object containing the saved message details
     * @throws UsernameNotFoundException if the user with the given ID is not found
     * @throws RuntimeException if the rental with the given ID is not found
     */
    @Transactional
    public SendMessageDTO sendMessage(SendMessageDTO sendMessageDTO) {

        User user = userRepository.findById(sendMessageDTO.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + sendMessageDTO.getUserId()));

        Rental rental = rentalRepository.findById(sendMessageDTO.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found with id: " + sendMessageDTO.getRentalId()));

        Message message = new Message();
        message.setUser(user);
        message.setRental(rental);
        message.setMessage(sendMessageDTO.getMessage());

        Message savedMessage = messageRepository.save(message);

        return mapper.toSendMessageDTO(savedMessage);
    }


}


