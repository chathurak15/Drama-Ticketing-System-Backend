package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.BookingRequestDTO;
import com.example.NatakaLK.dto.responseDTO.BookingResponseDTO;
import com.example.NatakaLK.model.Booking;
import com.example.NatakaLK.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/cancel")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<String> addBooking(@RequestParam int bookingId, @RequestParam int customerId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, customerId));
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('Admin','Customer','TheatreManager')")
    public ResponseEntity<BookingResponseDTO> getBooking(@RequestParam String ticketId) {
        return ResponseEntity.ok(bookingService.getBookingByTicketId(ticketId));
    }

    @GetMapping("/all/byUser")
    @PreAuthorize("hasAnyRole('Admin','Customer','TheatreManager')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingByUser(@RequestParam int userId) {
        return ResponseEntity.ok(bookingService.getAllBookingByUser(userId));
    }

}
