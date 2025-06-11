package com.example.NatakaLK.dto.requestDTO;

import com.example.NatakaLK.dto.responseDTO.ActorResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DramaUpdateDTO {
    private int id;
    private String title;
    private String description;
    private int duration;
    private String videoUrl;
    private String image;
    private List<Integer> actorIds;
}
