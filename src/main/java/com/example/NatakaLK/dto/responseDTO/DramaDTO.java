package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DramaDTO {
    private int id;
    private String title;
    private String description;
    private int duration;
    private String videoUrl;
    private String image;
    private List<ActorResponseDTO> Actors;
}
