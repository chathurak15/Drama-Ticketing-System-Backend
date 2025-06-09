package com.example.NatakaLK.controller;


import com.example.NatakaLK.dto.requestDTO.LoginDTO;
import com.example.NatakaLK.dto.requestDTO.RegisterDTO;
import com.example.NatakaLK.dto.responseDTO.LoginResponseDTO;
import com.example.NatakaLK.service.JwtService;
import com.example.NatakaLK.service.UserService;
import com.example.NatakaLK.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        String result = userService.registerUser(registerDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(jwtService.createJwtToken(loginDTO));

    }
}
