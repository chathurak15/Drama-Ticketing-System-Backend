package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitResponseDTO {
    private int bookingId;
    private String ticketId;
    private String merchantId;
    private String hash;
    private double amount;
    private String currency;
}
