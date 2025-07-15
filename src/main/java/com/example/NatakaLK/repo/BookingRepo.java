package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.beans.JavaBean;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
    Booking findByTicketId(String ticketId);

    List<Booking> findAllByUserId(int userId);

    Page<Booking> findByShowShowIdAndTicketIdContainingIgnoreCase(Integer showId, String ticketId, Pageable pageable);

    Page<Booking> findByShowShowId(Integer showId, Pageable pageable);
    Page<Booking> findByShowShowIdIn(List<Integer> showIds, Pageable pageable);
    Page<Booking> findByShowShowIdInAndTicketIdContainingIgnoreCase(List<Integer> showIds, String ticketId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b")
    double getTotalRevenue();
}
