package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketPricesDTO {
    private String category;
    private BigDecimal price;
    private int totalSeats;
    private int seatsPerRow;
}
