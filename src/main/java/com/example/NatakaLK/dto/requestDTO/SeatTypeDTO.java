package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatTypeDTO {
    private String typeName;
    private Integer totalSeats;
    private Integer seatsPerRow;
}
