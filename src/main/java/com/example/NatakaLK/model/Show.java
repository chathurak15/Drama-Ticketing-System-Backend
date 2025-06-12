package com.example.NatakaLK.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int showId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date showDate;

    @Column(nullable = false)
    private String showTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "drama_id", nullable = false)
    private Drama drama;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }
}
