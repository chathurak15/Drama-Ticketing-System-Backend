package com.example.NatakaLK.dto.requestDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DramaDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description too long")
    private String description;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int duration;
    private String videoUrl;

    @NotBlank(message = "Image is required")
    private String image;

    @NotEmpty(message = "At least one actor must be selected")
    private List<Integer> actorIds;
}
