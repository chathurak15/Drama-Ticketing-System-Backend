package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.ActorDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/actor")
@CrossOrigin
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> addActor(@RequestBody ActorDTO actorDTO) {
        return ResponseEntity.ok(actorService.addActor(actorDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDrama(
            @RequestParam int page,
            @RequestParam  int size,
            @RequestParam(required = false) String name) {

        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        PaginatedDTO actors = actorService.getAllActors(name,page,size);
        return ResponseEntity.ok(actors);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> deleteActor(@PathVariable int id) {
        return ResponseEntity.ok(actorService.deleteActor(id));
    }
}
