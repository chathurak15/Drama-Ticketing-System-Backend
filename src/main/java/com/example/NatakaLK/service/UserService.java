package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.RegisterDTO;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
