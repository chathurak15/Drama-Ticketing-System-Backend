package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.DramaDTO;
import com.example.NatakaLK.dto.responseDTO.DramaResponseDTO;
import com.example.NatakaLK.service.DramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/drama")
@CrossOrigin
public class DramaController {

    @Autowired
    private DramaService dramaService;

    @GetMapping("/all")
    public ResponseEntity<List<DramaResponseDTO>> getAllDrama() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> addDrama(@RequestBody DramaDTO dramaDTO) {
        return ResponseEntity.ok(dramaService.addDrama(dramaDTO));
    }
}
