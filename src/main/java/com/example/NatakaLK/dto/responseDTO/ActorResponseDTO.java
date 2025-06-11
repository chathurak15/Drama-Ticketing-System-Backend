package com.example.NatakaLK.dto.responseDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorResponseDTO {
    private int id;
    private String name;
    private String gender;
    private String birthday;
    private String photo;
}
