package com.projet3.controller;

import com.projet3.dto.MessageDTO;
import com.projet3.dto.SendMessageDTO;
import com.projet3.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody SendMessageDTO sendMessageDTO) {
        try {
            SendMessageDTO savedSendMessageDTO = messageService.sendMessage(sendMessageDTO);
            return ResponseEntity.ok(savedSendMessageDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Error sending message\"}");
        }
    }
}



