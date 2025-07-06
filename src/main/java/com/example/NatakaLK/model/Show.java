package com.example.NatakaLK.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @ToString.Exclude
    private City city;

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
    @ToString.Exclude
    private Drama drama;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShowPricing> showPricings;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LockedSeat> lockSeats;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }
}
