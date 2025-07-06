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
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            SeatResponseDTO seatResponseDTO = new SeatResponseDTO(
                    show.getTitle(),
                    show.getShowDate(),
                    show.getShowTime(),
                    show.getLocation(),
                    ticketPrices
            );
            return  seatResponseDTO;
        }else{
            throw new NotFoundException("s");
        }
    }

    public SeatStatusResponseDTO getUnavailableSeats(int showId) {
        showRepo.findById(showId)
                .orElseThrow(() -> new NotFoundException("Show is not available"));

        List<String> lockedSeats = lockedSeatRepo.getLockSeatIdsByShowId(showId);
        List<String> bookedSeats = bookedSeatRepo.getLockSeatIdsByShowId(showId);

        return new SeatStatusResponseDTO(bookedSeats, lockedSeats);
    }

    public String lockSeat(LockSeatRequestDTO lockSeatRequestDTO) {
        Show show = showRepo.findById(lockSeatRequestDTO.getShowId())
                .orElseThrow(() -> new NotFoundException("Show not found"));
        User user = userRepo.findById(lockSeatRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        LocalDateTime now = LocalDateTime.now();

        if (show.getShowDate().before(new Date())) {
            return "Show already scheduled! This show is not available.";
        }

        for (String seat : lockSeatRequestDTO.getSeats()) {
            LockedSeat existing = lockedSeatRepo.findLockedSeatBySeatId(seat);

            if (existing != null && existing.isLocked()) {
                return "Seat can't be locked! Already locked: " + seat;
            }

            LockedSeat lockedSeat = new LockedSeat();
            lockedSeat.setShow(show);
            lockedSeat.setUser(user);
            lockedSeat.setSeatId(seat);
            lockedSeat.setLocked(true);
            lockedSeat.setCreatedAt(now);

            lockedSeatRepo.save(lockedSeat);
        }

        return "Seats locked successfully";
    }


    @Scheduled(fixedRate = 60000)
    public void deleteExpiredLockedSeats() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        lockedSeatRepo.deleteByCreatedAtBefore(cutoffTime);
        System.out.println("Expired locked seats deleted at " + LocalDateTime.now());
    }
}
