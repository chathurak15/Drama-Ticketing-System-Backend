package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.BookingRequestDTO;
import com.example.NatakaLK.model.Booking;
import com.example.NatakaLK.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/booking")
@CrossOrigin
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<?> addBooking(@RequestBody BookingRequestDTO reqDTO) {
        return ResponseEntity.ok(bookingService.addBooking(reqDTO));
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<String> addBooking(@RequestParam Integer bookingId, @RequestParam Integer customerId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, customerId));
    }

}
