package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.SaveShowDTO;

import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/show")
@CrossOrigin
public class ShowController {
    @Autowired
    private ShowService showService;

    @PostMapping("add")
    @PreAuthorize("hasAnyRole('Admin','Organizer','TheatreManager')")
    public ResponseEntity<String> add(@RequestBody SaveShowDTO show) {
        if (show != null) {
            return ResponseEntity.ok(showService.addShow(show));
        }
        return ResponseEntity.ok("Something went wrong");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDrama(@RequestParam int page, @RequestParam  int size) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        PaginatedDTO shows = showService.getAll(page,size);
        return ResponseEntity.ok(shows);
    }
}
