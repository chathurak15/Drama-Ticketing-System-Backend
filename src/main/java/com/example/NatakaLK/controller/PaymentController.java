package com.example.NatakaLK.controller;

import com.example.NatakaLK.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "/notify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handlePayHereNotify(@RequestParam Map<String, String> formData) {
        System.out.println("Payment Notification: " + formData);
        String result = bookingService.confirmPayment(formData);
        return ResponseEntity.ok(result);
    }
}