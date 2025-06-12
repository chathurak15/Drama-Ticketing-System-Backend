package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.DramaRequestDTO;
import com.example.NatakaLK.dto.requestDTO.DramaUpdateDTO;
import com.example.NatakaLK.dto.responseDTO.DramaDTO;
import com.example.NatakaLK.dto.responseDTO.DramasResponseDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.service.DramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/drama")
@CrossOrigin
public class DramaController {

    @Autowired
    private DramaService dramaService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDrama(@RequestParam int page, @RequestParam  int size) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        PaginatedDTO dramas = dramaService.getAll(page,size);
        return ResponseEntity.ok(dramas);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> addDrama(@RequestBody DramaRequestDTO dramaDTO) {
        return ResponseEntity.ok(dramaService.addDrama(dramaDTO));
    }

    //get drama by id
    @GetMapping("/{id}")
    public ResponseEntity<DramaDTO> getUser(@PathVariable int id) {
            DramaDTO dramaDTO = dramaService.getDramaById(id);
            return ResponseEntity.ok(dramaDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> deleteDrama(@PathVariable int id) {
        if (id >0){
            return ResponseEntity.ok(dramaService.deleteDrama(id));
        }
        return ResponseEntity.ok("Something went wrong");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> updateDrama(@RequestBody DramaUpdateDTO dramaUpdateDTO) {
        if (dramaUpdateDTO !=null){
            return ResponseEntity.ok(dramaService.updateDrama(dramaUpdateDTO));
        }
        return ResponseEntity.ok("Something went wrong");
    }

    @GetMapping("/find")
    public ResponseEntity<?> searchDrama(
            @RequestParam String title,@RequestParam int page, @RequestParam  int size) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        return ResponseEntity.ok(dramaService.searchDramaByTitle(title,page,size));
    }
}
