package com.example.NatakaLK.dto.responseDTO;

import com.example.NatakaLK.dto.requestDTO.SeatBookingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookingResponseDTO {
    private int Id;
    private String ticketId;
    private double totalAmount;
    private String status;
    private LocalDateTime created_at;
    private int seatCount;
    private String theatreName;
    private int showId;
    private List<SeatBookingInfo> seats;
    private String showTitle;
    private String showTime;
    private Date showDate;
    private String location;
}
