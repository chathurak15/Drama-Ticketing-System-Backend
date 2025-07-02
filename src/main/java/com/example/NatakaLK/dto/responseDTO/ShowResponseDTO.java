package com.example.NatakaLK.dto.responseDTO;

import com.example.NatakaLK.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShowResponseDTO {
    private int showId;
    private String title;
    private String description;
    private String image;
    private String location;
    private Date showDate;
    private String showTime;
    private DramasResponseDTO drama;
    private String status;
    private UserResponseDTO user;
    private CityDTO city;
}
