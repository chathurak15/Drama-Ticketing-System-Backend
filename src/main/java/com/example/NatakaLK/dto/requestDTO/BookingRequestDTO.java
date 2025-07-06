package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDTO {
    private int showId;
    private int userId;
    private List<SeatBookingInfo> seats;
    private double totalAmount;
}
