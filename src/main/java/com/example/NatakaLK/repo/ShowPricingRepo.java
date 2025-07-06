package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.SeatType;
import com.example.NatakaLK.model.Show;
import com.example.NatakaLK.model.ShowPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowPricingRepo extends JpaRepository<ShowPricing, Long> {

    List<ShowPricing> findByShow(Show show);
}
