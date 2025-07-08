package com.example.NatakaLK.dto.requestDTO;

import com.example.NatakaLK.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String fname;
    private String lname;
    private String email;
    private String phoneNumber;
    private String status;
    private RoleType role;
    private String image;
}
