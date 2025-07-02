package com.example.NatakaLK.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    //used to persist only the date (not time) for the birthday field
    @Temporal(TemporalType.DATE)
    private Date birthday;

    private String photo;

    @ManyToMany(mappedBy = "actors")
    private Set<Drama> dramas;
}
