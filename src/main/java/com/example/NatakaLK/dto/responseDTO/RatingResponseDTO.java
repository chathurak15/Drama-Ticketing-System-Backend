package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponseDTO {
    private int id;
    private double rating;
    private Date created;
    private String comment;
    private UserDTO User;
}
