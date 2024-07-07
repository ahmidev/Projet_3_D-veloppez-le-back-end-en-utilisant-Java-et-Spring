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


