package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.SeatType;
import com.example.NatakaLK.model.Theatre;
import com.example.NatakaLK.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreRepo extends JpaRepository<Theatre, Long> {
    List<Theatre> findByCreatedBy(User user);

}
