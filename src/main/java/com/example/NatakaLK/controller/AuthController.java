package com.example.NatakaLK.controller;
import com.example.NatakaLK.dto.requestDTO.LoginDTO;
import com.example.NatakaLK.dto.requestDTO.RegisterDTO;
import com.example.NatakaLK.dto.responseDTO.LoginResponseDTO;
import com.example.NatakaLK.dto.responseDTO.UserResponseDTO;
import com.example.NatakaLK.service.JwtService;
import com.example.NatakaLK.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private ResponseEntity<UserResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO loginResponseDTO = jwtService.createJwtToken(loginDTO);

        ResponseCookie cookie = ResponseCookie.from("JWT", loginResponseDTO.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(2*60*60)
                .sameSite("None")
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDTO.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

}
