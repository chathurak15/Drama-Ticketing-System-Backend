package com.example.NatakaLK.dto.responseDTO;

import com.example.NatakaLK.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String fname;
    private String lname;
    private String email;
    private String phoneNumber;
    private String status;
    private RoleType role;
}
