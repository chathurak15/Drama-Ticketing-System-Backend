package com.example.NatakaLK.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LockSeatRequestDTO {
    private int showId;
    private int userId;
    private double totalAmount;
    private List<String> seats;
}
