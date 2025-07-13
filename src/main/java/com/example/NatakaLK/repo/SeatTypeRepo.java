package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.SeatType;
import com.example.NatakaLK.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatTypeRepo extends JpaRepository<SeatType, Long> {
    List<SeatType> findByTheatre(Theatre theatre);
    List<SeatType> findByTheatreId(Long theatreId);

    void deleteAllByTheatreId(Long id);
}
