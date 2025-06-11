package com.example.NatakaLK.dto.responseDTO;
import com.example.NatakaLK.model.Actor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DramaResponseDTO {
    private String drama;
    private String title;
    private String description;
    private int duration;
    private String videoUrl;
    private String image;
    private List<Actor> actors;
}
