package com.example.NatakaLK.dto.responseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DramasResponseDTO {
    private int id;
    private String title;
    private String description;
    private int duration;
    private String videoUrl;
    private String image;
}
