package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.LockSeatRequestDTO;
import com.example.NatakaLK.dto.responseDTO.SeatResponseDTO;
import com.example.NatakaLK.dto.responseDTO.SeatStatusResponseDTO;
import com.example.NatakaLK.dto.responseDTO.TicketPricesDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.LockedSeat;
import com.example.NatakaLK.model.Show;
import com.example.NatakaLK.model.ShowPricing;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeatService {
    @Autowired
    private ShowPricingRepo showPricingRepo;

    @Autowired
    private LockedSeatRepo lockedSeatRepo;

    @Autowired
    private BookedSeatRepo bookedSeatRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShowRepo showRepo;

    public SeatResponseDTO getSeatPlan(Integer showId) {
        if (showRepo.existsById(showId)) {
            Show show = showRepo.findById(showId).get();
            List<ShowPricing> showPricings = showPricingRepo.findByShow(show);
            List<TicketPricesDTO> ticketPrices = new ArrayList<>();

            for (ShowPricing showPricing : showPricings) {
                TicketPricesDTO ticketPricesDTO = new TicketPricesDTO();
                ticketPricesDTO.setPrice(showPricing.getPrice());
                ticketPricesDTO.setTotalSeats(showPricing.getSeatType().getTotalSeats());
                ticketPricesDTO.setSeatsPerRow(showPricing.getSeatType().getSeatsPerRow());
                ticketPricesDTO.setCategory(showPricing.getSeatType().getTypeName());
                ticketPrices.add(ticketPricesDTO);
            }
            return new SeatResponseDTO(
                    show.getTitle(),
                    show.getShowDate(),
                    show.getShowTime(),
                    show.getLocation(),
                    ticketPrices
            );
        } else {
            throw new NotFoundException("Show not found");
        }
    }

    public SeatStatusResponseDTO getUnavailableSeats(int showId) {
        showRepo.findById(showId)
                .orElseThrow(() -> new NotFoundException("Show is not available"));

        // Locked seats currently active
        List<String> lockedSeats = lockedSeatRepo.getLockSeatIdsByShowId(showId);
        // Permanently booked seats
        List<String> bookedSeats = bookedSeatRepo.getBookedSeatByShowId(showId);

        return new SeatStatusResponseDTO(bookedSeats, lockedSeats);
    }

    public String lockSeat(LockSeatRequestDTO lockSeatRequestDTO) {
        Show show = showRepo.findById(lockSeatRequestDTO.getShowId())
                .orElseThrow(() -> new NotFoundException("Show not found"));
        User user = userRepo.findById(lockSeatRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        LocalDateTime now = LocalDateTime.now();

        // Date check
        // Note: java.util.Date usage is tricky with LocalDateTime, but keeping your logic for now
        if (show.getShowDate().before(new Date())) {
            throw new RuntimeException("Show already scheduled! This show is not available.");
        }

        try {
            for (String seatId : lockSeatRequestDTO.getSeats()) {

                // Check if seat is already booked (Sold)
                boolean isBooked = bookedSeatRepo.existsBySeatIdAndShowAndIsBookedTrue(seatId, show);
                if (isBooked) {
                    throw new RuntimeException("Seat is already permanently booked: " + seatId);
                }

                // 2. Check if seat is already locked (Temporary Lock)
                // We assume you have a method `existsBySeatIdAndShow` in LockedSeatRepo
                boolean isLocked = lockedSeatRepo.existsBySeatIdAndShow(seatId, show);

                if (isLocked) {
                    throw new RuntimeException("Seat is currently selected by another user: " + seatId);
                }

                // 3. Create Lock Object
                LockedSeat lockedSeat = new LockedSeat();
                lockedSeat.setShow(show);
                lockedSeat.setUser(user);
                lockedSeat.setSeatId(seatId);
                lockedSeat.setLocked(true);
                lockedSeat.setCreatedAt(now);

                // 4. Save (This is where the Race Condition is caught!)
                lockedSeatRepo.save(lockedSeat);

                // Force flush to trigger DB constraint immediately within the loop
                lockedSeatRepo.flush();
            }
            return "Seats locked successfully";

        } catch (DataIntegrityViolationException e) {
            // This catches the case where two users clicked at the exact same millisecond
            // The Database Unique Constraint throws this error
            throw new RuntimeException("One or more seats were just selected by someone else. Please try again.");
        }
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredLockedSeats() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        lockedSeatRepo.deleteByCreatedAtBefore(cutoffTime);
    }
}