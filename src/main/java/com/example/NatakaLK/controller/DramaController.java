package com.example.NatakaLK.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drama")
@CrossOrigin
public class DramaController {

    @GetMapping("/all")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> getAllDrama() {
        return ResponseEntity.ok("All drama");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> addDrama() {
        return ResponseEntity.ok("Added drama");
    }
}
