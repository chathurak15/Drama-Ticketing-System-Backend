package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.LockedSeat;
import com.example.NatakaLK.model.Show;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LockedSeatRepo extends JpaRepository<LockedSeat, Long> {
    LockedSeat findLockedSeatBySeatId(String seat);

    @Query("SELECT l.seatId FROM LockedSeat l WHERE l.show.showId = :showId AND l.isLocked = true")
    List<String> getLockSeatIdsByShowId(@Param("showId") int showId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LockedSeat ls WHERE ls.createdAt < :cutoffTime")
    void deleteByCreatedAtBefore(LocalDateTime cutoffTime);

    @Query("SELECT l FROM LockedSeat l WHERE l.seatId = :seat AND l.show = :show")
    LockedSeat findLockedSeatBySeatIdAndShow(@Param("seat") String seat, @Param("show") Show show);
}
