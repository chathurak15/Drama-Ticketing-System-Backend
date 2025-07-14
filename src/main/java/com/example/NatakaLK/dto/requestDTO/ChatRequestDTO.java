package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDTO {
    private String model; // e.g., llama3, llama3:8b, etc.
    private List<ChatMessageDTO> messages;
}
