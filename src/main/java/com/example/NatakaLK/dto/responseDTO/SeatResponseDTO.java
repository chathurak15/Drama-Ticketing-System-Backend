package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SeatResponseDTO {
    private String title;
    private Date showDate;
    private String showTime;
    private String location;
    private List<TicketPricesDTO> ticketPrices;
}
