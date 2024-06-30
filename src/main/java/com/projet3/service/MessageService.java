package com.projet3.service;

import com.projet3.dto.MessageDTO;
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

@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        User user = userRepository.findById(messageDTO.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + messageDTO.getUserId()));

        Rental rental = rentalRepository.findById(messageDTO.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found with id: " + messageDTO.getRentalId()));

        Message message = new Message();
        message.setUser(user);
        message.setRental(rental);
        message.setMessage(messageDTO.getMessage());

        Message savedMessage = messageRepository.save(message);

        return convertToDTO(savedMessage);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setUserId(message.getUser().getId());
        messageDTO.setRentalId(message.getRental().getId());
        messageDTO.setMessage(message.getMessage());
        messageDTO.setCreatedAt(message.getCreatedAt());
        messageDTO.setUpdatedAt(message.getUpdatedAt());
        return messageDTO;
    }
}


