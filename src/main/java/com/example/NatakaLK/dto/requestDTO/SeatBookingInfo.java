package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatBookingInfo {
    private String seatIdentifier;
    private double price;
}
