package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.LockedSeat;
import com.example.NatakaLK.model.Show;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LockedSeatRepo extends JpaRepository<LockedSeat, Long> {

//    LockedSeat findLockedSeatBySeatId(String seat);

    @Query("SELECT l.seatId FROM LockedSeat l WHERE l.show.showId = :showId AND l.isLocked = true")
    List<String> getLockSeatIdsByShowId(@Param("showId") int showId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LockedSeat ls WHERE ls.createdAt < :cutoffTime")
    void deleteByCreatedAtBefore(LocalDateTime cutoffTime);

    // Return Optional to avoid NullPointerExceptions
    @Query("SELECT l FROM LockedSeat l WHERE l.seatId = :seat AND l.show = :show")
    Optional<LockedSeat> findLockedSeatBySeatIdAndShow(@Param("seat") String seat, @Param("show") Show show);

    // Added for the Race Condition check in SeatService
    boolean existsBySeatIdAndShow(String seatId, Show show);
}