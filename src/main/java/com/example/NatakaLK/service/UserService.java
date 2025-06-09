package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.RegisterDTO;
import com.example.NatakaLK.dto.responseDTO.UserResponseDTO;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(RegisterDTO registerDTO){

        //User email check before register
        if (userRepo.existsByEmail(registerDTO.getEmail())) {
            return "Email already in use";
        }

        // Map DTO to Entity
        User user = modelMapper.map(registerDTO, User.class);
        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());
        user.setPassword(hashedPassword);

        // Set approval status
        if (registerDTO.getRole().equals("User")) {
            user.setStatus("approved");
        } else {
            user.setStatus("pending");
        }
        try {
            userRepo.save(user);
            return "User registered successfully";
        } catch (Exception e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    // Retrieves a paginated list of users sorted by ID in descending order.
    public Page<UserResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "id"));
        Page<User> users = userRepo.findAll(pageable);

        // Map each User entity to a UserResponseDTO and return the mapped Page
        return users.map(user -> modelMapper.map(user,UserResponseDTO.class));
    }

    //check status value validation, Find user by ID, Update user status and save
    public String updateUserStatus(int id, String status) {
        List<String> validStatuses = List.of("approved", "pending", "rejected");
        if (!validStatuses.contains(status.toLowerCase())) {
            return "Invalid status";
        }
        if (!userRepo.findById(id).isEmpty()) {
            User user = userRepo.findById(id).get();
            user.setStatus(status);
            userRepo.save(user);
            return "User updated successfully";
        }else {
            return "User not found";
        }
    }

    public String updateUser(RegisterDTO registerDTO) {
            // Find existing user by email
        User user = userRepo.findByEmail(registerDTO.getEmail());
        if (user == null) {
            return  "User with email " + registerDTO.getEmail() + " not found";
        }
        user.setFname(registerDTO.getFname());
        user.setLname(registerDTO.getLname());
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepo.save(user);
        return "User updated successfully";
    }
}
