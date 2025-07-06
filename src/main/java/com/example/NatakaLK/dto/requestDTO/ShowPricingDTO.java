package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowPricingDTO {
    private Long seatTypeId;
    private BigDecimal price;
}
