package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.RatingDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/getByDrama")
    public ResponseEntity<PaginatedDTO> getRatingValue(
            @RequestParam int page, @RequestParam int size, @RequestParam int dramaId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByDrama(page,size,dramaId));
    }

    @PreAuthorize("hasRole('User')")
    @PostMapping("/add")
    public ResponseEntity<String> submitRating(@RequestBody RatingDTO dto) {
        return ResponseEntity.ok( ratingService.submitRating(dto));
    }

    @GetMapping("/average/{dramaId}")
    public ResponseEntity<?> getAverageRating(@PathVariable int dramaId) {
        Double avg = ratingService.getAverageRating(dramaId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }
}
