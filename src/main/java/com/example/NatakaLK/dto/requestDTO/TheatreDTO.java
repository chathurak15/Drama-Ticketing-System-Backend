package com.example.NatakaLK.dto.requestDTO;

import com.example.NatakaLK.model.enums.AreaType;
import com.example.NatakaLK.model.enums.TheatreStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreDTO {
    private String name;
    private AreaType areaType;
    private TheatreStatus status;
    private Integer totalCapacity; // For open areas
    private BigDecimal openAreaPrice; // For open areas
    private Long showId; // For temporary theaters
    private List<SeatTypeDTO> seatTypes;
}
