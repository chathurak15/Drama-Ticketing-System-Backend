package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.LockSeatRequestDTO;
import com.example.NatakaLK.dto.responseDTO.SeatResponseDTO;
import com.example.NatakaLK.dto.responseDTO.SeatStatusResponseDTO;
import com.example.NatakaLK.dto.responseDTO.TicketPricesDTO;
import com.example.NatakaLK.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/seat-plan/{showId}")
    public ResponseEntity<SeatResponseDTO> getShow(@PathVariable int showId) {
        return ResponseEntity.ok(seatService.getSeatPlan(showId));
    }

    @GetMapping("/unavailable-seats/{showId}")
    public ResponseEntity<SeatStatusResponseDTO> getUnavailableSeats(@PathVariable int showId) {
        SeatStatusResponseDTO seatStatusResponseDTO = seatService.getUnavailableSeats(showId);
        return ResponseEntity.ok(seatStatusResponseDTO);
    }

    @PostMapping("/lock-seats")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<String> lockSeat (@RequestBody LockSeatRequestDTO lockSeatRequestDTO) {
        return ResponseEntity.ok(seatService.lockSeat(lockSeatRequestDTO));
    }

}
