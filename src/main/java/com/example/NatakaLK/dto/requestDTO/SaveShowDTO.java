package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SaveShowDTO {
    private String title;
    private String description;
    private String image;
    private String location;
    private int cityId;
    private Date showDate;
    private String showTime;
    private int dramaId;
    private int userId;
    private Long theaterId; // For existing theaters
    private TheatreDTO temporaryTheatre; // For new temporary theaters
    private List<ShowPricingDTO> pricing;
}
