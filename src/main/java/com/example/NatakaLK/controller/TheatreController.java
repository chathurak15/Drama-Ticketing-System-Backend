package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.TheatreDTO;
import com.example.NatakaLK.dto.responseDTO.SeatTypeResponseDTO;
import com.example.NatakaLK.dto.responseDTO.TheatreResponseDTO;
import com.example.NatakaLK.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/theatre")
@CrossOrigin
public class TheatreController {
    @Autowired
    private TheatreService theatreService;

    @GetMapping("/all/{id}")
    @PreAuthorize("hasAnyRole('Admin','TheatreManager','Organizer')")
    public ResponseEntity<?> getTheatre(@PathVariable int id) {
        List<TheatreResponseDTO> theatreResponseDTOS = theatreService.getTheatersByManager(id);
        return  ResponseEntity.ok(theatreResponseDTOS);
    }

    @GetMapping("/seat-types/{id}")
    @PreAuthorize("hasAnyRole('Admin','TheatreManager','Organizer')")
    public ResponseEntity<List<SeatTypeResponseDTO>> getSeatTypesByTheatreId(@PathVariable long id) {
        List<SeatTypeResponseDTO> seatTypeResponseDTOs = theatreService.getSeatTypesByTheatreId(id);
        return  ResponseEntity.ok(seatTypeResponseDTOs);
    }

    @PostMapping("/permanent/{managerId}")
    @PreAuthorize("hasAnyRole('Admin','TheatreManager')")
    public ResponseEntity<String> createPermanentTheater(@RequestBody TheatreDTO theatreDTO, @PathVariable int managerId) {
        return ResponseEntity.ok(theatreService.createPermanentTheatre(theatreDTO, managerId));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('Admin','Organizer','TheatreManager')")
    public ResponseEntity<String> deleteTheatre(@RequestParam long theatreId, @RequestParam int id) {
        if (id >0){
            return ResponseEntity.ok(theatreService.deleteTheatre(theatreId, id));
        }
        return ResponseEntity.ok("Something went wrong");
    }
}
