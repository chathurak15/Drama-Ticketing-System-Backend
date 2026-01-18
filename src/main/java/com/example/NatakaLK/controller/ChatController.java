package com.example.NatakaLK.controller;

import com.example.NatakaLK.service.OllamaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/chat")
@CrossOrigin
public class ChatController {

    private final OllamaService ollamaService;

    public ChatController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('Admin','TheatreManager','Customer')")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        String reply = ollamaService.getReply(userMessage);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

}

