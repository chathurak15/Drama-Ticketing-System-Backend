package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.BookedSeat;
import com.example.NatakaLK.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookedSeatRepo extends JpaRepository<BookedSeat, Integer> {

    @Query("SELECT b FROM BookedSeat b WHERE b.seatId = :seat AND b.show = :show")
    BookedSeat findBookedSeatBySeatIdAndShow(@Param("seat") String seat, @Param("show") Show show);

    @Query("SELECT b.seatId FROM BookedSeat b WHERE b.show.showId = :showId AND b.isBooked = true")
    List<String> getLockSeatIdsByShowId(int showId);

    List<BookedSeat> findAllByBooking_Id(int bookingId);
}
