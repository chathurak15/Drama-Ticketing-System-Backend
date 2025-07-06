package com.example.NatakaLK.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String seatId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isLocked;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    private LocalDateTime createdAt = LocalDateTime.now();
}
