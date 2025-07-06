package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatTypeResponseDTO {
    private Long id;
    private String typeName;
    private Integer totalSeats;
    private Integer seatsPerRow;
}
