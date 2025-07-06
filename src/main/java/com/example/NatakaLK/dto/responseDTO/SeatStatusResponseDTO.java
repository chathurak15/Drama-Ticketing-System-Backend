package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatStatusResponseDTO {
    private List<String> bookedSeats;
    private List<String> lockedSeats;

}
