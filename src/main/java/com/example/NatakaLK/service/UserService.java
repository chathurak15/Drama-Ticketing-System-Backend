package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.dto.requestDTO.RegisterDTO;
import com.example.NatakaLK.dto.responseDTO.UserResponseDTO;
import com.example.NatakaLK.exception.NotFoundException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(RegisterDTO registerDTO){

        if ("Admin".equalsIgnoreCase(String.valueOf(registerDTO.getRole()))) {
            return "Try again! Incorrect user role";
        }
        //User email check before register
        if (userRepo.existsByEmail(registerDTO.getEmail())) {
            return "Email already in use";
        }

        // Map DTO to Entity
        User user = modelMapper.map(registerDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // Set approval status
        if ("User".equalsIgnoreCase(String.valueOf(registerDTO.getRole()))) {
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
    public PaginatedDTO getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "id"));
        Page<User> users = userRepo.findAll(pageable);
        if (users.hasContent() ) {
            List<UserResponseDTO> userResponseDTOS = users.getContent()
                    .stream().map(user -> modelMapper.map(user, UserResponseDTO.class))
                    .collect(Collectors.toList());

            PaginatedDTO paginatedDTO = new PaginatedDTO();
            paginatedDTO.setContent(userResponseDTOS);
            paginatedDTO.setTotalItems(users.getTotalElements());
            paginatedDTO.setTotalPages(users.getTotalPages());
            return paginatedDTO;
        }else {
            throw new NotFoundException("No users found");
        }
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
        user.setImage(registerDTO.getImage());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepo.save(user);
        return "User updated successfully";
    }


    public UserResponseDTO getUserById(int id) {
        if (!userRepo.existsById(id)) {
            return null;
        }
        User user = userRepo.findById(id).get();
        if (user == null) {
            return null;
        }
        return modelMapper.map(user,UserResponseDTO.class);
    }

    public String deleteUser(int id) {
        if (!userRepo.existsById(id) || id==6) {
            return "User not found";
        }
        userRepo.deleteById(id);
        return "User deleted successfully";
    }
}
