package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
}
