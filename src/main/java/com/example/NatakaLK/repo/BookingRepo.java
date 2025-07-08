package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
    Booking findByTicketId(String ticketId);

    List<Booking> findAllByUserId(int userId);
}
