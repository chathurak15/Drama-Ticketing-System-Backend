package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.LoginDTO;
import com.example.NatakaLK.dto.responseDTO.LoginResponseDTO;
import com.example.NatakaLK.dto.responseDTO.UserResponseDTO;
import com.example.NatakaLK.repo.UserRepo;
import com.example.NatakaLK.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private com.example.NatakaLK.model.User user;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        user = userRepo.findByEmail(email);
        if (user != null) {
            String password = user.getPassword() != null ? user.getPassword() : "";
            return new User(user.getEmail(), password, getAuthority(user));
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }


    private Set getAuthority(com.example.NatakaLK.model.User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user.getRole() != null) {
            String roleName = user.getRole().toString().toUpperCase(); 
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        }
        return grantedAuthorities;
    }

    public LoginResponseDTO createJwtToken(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        try {
            authenticate(email,password);
            UserDetails userDetails = loadUserByUsername(email);
            String token = jwtUtil.generateToken(userDetails);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(modelMapper.map(user, UserResponseDTO.class), token );
            return loginResponseDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (BadCredentialsException e){
            throw new Exception("Bad credentials",e);
        }
    }

    public String generateTokenByEmail(String email) {
        UserDetails userDetails = loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }

}
