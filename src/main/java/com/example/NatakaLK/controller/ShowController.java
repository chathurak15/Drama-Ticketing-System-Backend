package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.SaveShowDTO;
import com.example.NatakaLK.dto.requestDTO.UpdateShowDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.dto.responseDTO.ShowResponseDTO;
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

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('Admin','Organizer','TheatreManager')")
    public ResponseEntity<String> add(@RequestBody SaveShowDTO show) {
        if (show != null) {
            return ResponseEntity.ok(showService.addShow(show));
        }
        return ResponseEntity.ok("Something went wrong");
    }

    @GetMapping("/all/manage")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getAllShowAdmin(@RequestParam int page, @RequestParam  int size, @RequestParam String status) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        PaginatedDTO shows = showService.getAll(page,size,status);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllShow(@RequestParam int page,
                                        @RequestParam  int size,
                                        @RequestParam(required = false) String title,
                                        @RequestParam(required = false) String date,
                                        @RequestParam(required = false) Integer city,
                                        @RequestParam(required = false) String location,
                                        @RequestParam(required = false) Integer dramaId) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        PaginatedDTO shows = showService.getAllByApproved(page,size,title,date,city,location,dramaId);
        return ResponseEntity.ok(shows);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ShowResponseDTO> getShow(@PathVariable int id) {
        ShowResponseDTO showResponseDTO = showService.getShowById(id);
        return ResponseEntity.ok(showResponseDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('Admin','Organizer','TheatreManager')")
    public ResponseEntity<String> deleteShow(@RequestParam int showId, @RequestParam int id) {
        if (id >0){
            return ResponseEntity.ok(showService.deleteShow(showId, id));
        }
        return ResponseEntity.ok("Something went wrong");
    }
//
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('Admin','Organizer','TheatreManager')")
    public ResponseEntity<String> updateShow( @RequestBody UpdateShowDTO updateShowDTO) {
        if (updateShowDTO !=null){
            return ResponseEntity.ok(showService.updateShow(updateShowDTO));
        }
        return ResponseEntity.ok("Something went wrong");
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> updateUserStatus(
            @RequestParam int id,
            @RequestParam String status) {
        return ResponseEntity.ok(showService.updateShowStatus(id,status));
    }

    @GetMapping("/all/user")
    @PreAuthorize("hasAnyRole('Admin','Organizer','TheatreManager')")
    public ResponseEntity<?> getAllShowByUser(
                                        @RequestParam int page,
                                        @RequestParam  int size,
                                        @RequestParam(required = false) String title,
                                        @RequestParam Integer userId, @RequestParam(required = false) String status) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        PaginatedDTO shows = showService.getAllByUserId(page,size,title,userId,status);
        return ResponseEntity.ok(shows);
    }


    @GetMapping("/get-shows-by-drama-id")
    public ResponseEntity<?> getShowsByDramaId(@RequestParam int page, @RequestParam  int size,@RequestParam int dramaId) {
        if (size >50){
            return ResponseEntity.ok("Item size is too large! Maximum allowed is 50.");
        }
        return ResponseEntity.ok(showService.getShowsByDramaId(dramaId,page,size));
    }
}
