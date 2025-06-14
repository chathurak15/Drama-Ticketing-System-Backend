package com.example.NatakaLK.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private double ratingValue;

    private String comment;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date created;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "drama_id", nullable = false)
    private Drama drama;

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }
}
