package com.example.NatakaLK.model;

import com.example.NatakaLK.model.enums.AreaType;
import com.example.NatakaLK.model.enums.TheatreStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "theatres")
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AreaType areaType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TheatreStatus status;

    // For open areas
    @Column(name = "total_capacity")
    private Integer totalCapacity;

    // For open areas
    @Column(name = "open_area_price", precision = 10, scale = 2)
    private BigDecimal openAreaPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    private Show show;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatType> seatTypes;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings ;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
