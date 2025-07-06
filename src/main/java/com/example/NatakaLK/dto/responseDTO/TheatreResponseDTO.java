package com.example.NatakaLK.dto.responseDTO;

import com.example.NatakaLK.model.enums.TheatreStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreResponseDTO {
    private Long id;
    private String name;
    private TheatreStatus status;
    private Integer totalCapacity;
    private BigDecimal openAreaPrice;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private List<SeatTypeResponseDTO> seatTypes;
}
