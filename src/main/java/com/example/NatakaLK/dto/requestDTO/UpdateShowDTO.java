package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateShowDTO {
    private int showId;
    private String title;
    private String description;
    private String image;
    private String location;
    private Date showDate;
    private String showTime;
    private int dramaId;
    private int userId;

}
