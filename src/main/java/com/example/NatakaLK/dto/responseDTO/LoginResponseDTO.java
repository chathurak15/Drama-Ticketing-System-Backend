package com.example.NatakaLK.dto.responseDTO;

import com.example.NatakaLK.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private UserResponseDTO user;
    private String token;

}
