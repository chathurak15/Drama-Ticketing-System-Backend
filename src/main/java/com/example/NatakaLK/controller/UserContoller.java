package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.paginated.PaginatedDTO;
import com.example.NatakaLK.dto.requestDTO.RegisterDTO;
import com.example.NatakaLK.dto.responseDTO.UserResponseDTO;
import com.example.NatakaLK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserContoller {
    @Autowired
    private UserService userService;

    //get all user information with pagination. only admin
    @GetMapping("/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?>getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam int size) {
        if (size > 50){
            return ResponseEntity.badRequest().body("Item size is too large! Maximum allowed is 50.");
        }else {
            PaginatedDTO users = userService.getAll(page, size);
            return ResponseEntity.ok(users);
        }
    }

    //update User Status only admin
    @PutMapping("/status")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> updateUserStatus(
            @RequestParam int id,
            @RequestParam String status) {
        return ResponseEntity.ok(userService.updateUserStatus(id,status));
    }


    //update User details
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('Admin','User','Organizer','TheatreManager')")
    public ResponseEntity<String> updateUser(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(userService.updateUser(registerDTO));
    }

    //get user by userid
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin','User','Organizer','TheatreManager')")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable int id) {
        if (id != 3){
            UserResponseDTO user = userService.getUserById(id);
                return ResponseEntity.ok(user);
        }
        return ResponseEntity.ok(new UserResponseDTO());
    }

    //delete user by userid
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
            return ResponseEntity.ok(userService.deleteUser(id));
    }
}
