package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateShowDTO {
    private int showId;
    private String title;
    private String description;
    private String image;
    private String location;
    private int cityId;
    private Date  showDate;
    private String showTime;
    private int dramaId;
    private int userId;
    private Long theaterId;
    private TheatreDTO temporaryTheatre;
    private List<ShowPricingDTO> pricing;

}
