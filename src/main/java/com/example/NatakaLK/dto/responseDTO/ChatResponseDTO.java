package com.example.NatakaLK.dto.responseDTO;

import com.example.NatakaLK.dto.requestDTO.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDTO {
    private ChatMessageDTO message;
    private boolean done;
}
