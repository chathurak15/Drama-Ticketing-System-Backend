package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.BookingRequestDTO;
import com.example.NatakaLK.dto.requestDTO.SeatBookingInfo;
import com.example.NatakaLK.dto.responseDTO.BookingResponseDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.*;
import com.example.NatakaLK.repo.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BookingService {

    @Autowired
    private ShowRepo showRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LockedSeatRepo lockedSeatRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BookedSeatRepo bookedSeatRepo;
    @Autowired
    private EmailService emailService;

    public String addBooking(BookingRequestDTO reqDTO) {
        Show show = showRepo.findById(reqDTO.getShowId())
                .orElseThrow(() -> new IllegalStateException("Show not found"));

        User user = userRepo.findById(reqDTO.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<SeatBookingInfo> seats = reqDTO.getSeats();
        List<String> seatsId = new ArrayList<>();

        //Validate all seats first
        for (SeatBookingInfo seatInfo : seats) {
            String seat = seatInfo.getSeatIdentifier().trim();
            seatsId.add(seat);

            LockedSeat lockedSeat = lockedSeatRepo.findLockedSeatBySeatIdAndShow(seat, show);
            BookedSeat bookedSeat = bookedSeatRepo.findBookedSeatBySeatIdAndShow(seat, show);

            if (lockedSeat != null && lockedSeat.isLocked()) {
                if (lockedSeat.getUser() == null || !lockedSeat.getUser().getId().equals(user.getId())) {
                    throw new NotFoundException("Seat " + seat + " is currently locked by another user");
                }
            }

            if (bookedSeat != null) {
                throw new NotFoundException("Seat " + seat + " is already booked");
            }
        }

        // All seats are valid - proceed to save
        Booking booking = new Booking();
        booking.setShow(show);
        booking.setUser(user);
        booking.setTotalAmount(reqDTO.getTotalAmount());;
        booking.setBookingDate(LocalDateTime.now());
        booking.setTheatre(show.getTheatre());
        booking.setTicketId("0");
        booking.setStatus("Confirmed");
        booking.setSeatCount(seats.size());
        bookingRepo.save(booking);
        booking.setTicketId(generateTicketId(show.getTheatre(), show, seatsId, booking.getId()));
        bookingRepo.save(booking);

        for (SeatBookingInfo seatInfo : seats) {
            String seatId = seatInfo.getSeatIdentifier().trim();
            double price = seatInfo.getPrice();
            BookedSeat bookedSeat = new BookedSeat();
            bookedSeat.setSeatId(seatId);
            bookedSeat.setShow(show);
            bookedSeat.setBooked(true);
            bookedSeat.setBooking(booking);
            bookedSeat.setPrice(price);
            bookedSeatRepo.save(bookedSeat);

            LockedSeat lockedSeat = lockedSeatRepo.findLockedSeatBySeatIdAndShow(seatId, show);
            if (lockedSeat != null && lockedSeat.isLocked()) {
                lockedSeat.setLocked(false);
                lockedSeatRepo.save(lockedSeat);
            }
        }
        emailService.sendTicketEmail(user.getEmail(),getBookingByTicketId(booking.getTicketId()));
        return booking.getTicketId();
    }

    //Generate unique TicketId(String)
    private String generateTicketId(Theatre theatre, Show show, List<String> seats, int bookingId) {
        String prefix = theatre.getName().substring(0, 3).toUpperCase(); // e.g., "REG"
        String showIdPart = String.format("%03d", show.getShowId());    // e.g., "007"
        String firstSeatPart = seats.get(0).replaceAll("[^A-Za-z0-9]", "").toUpperCase(); // e.g., "VIPA1"
        String bookingPart = String.format("%05d", bookingId); // Always unique

        return prefix + "-" + showIdPart + "-" + firstSeatPart + "-" + bookingPart;
    }

    public String cancelBooking(int bookingId, int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));

        if (bookingRepo.findById(bookingId).isPresent()){
            Booking booking = bookingRepo.findById(bookingId).get();
            if (booking.getShow().getShowDate().before(new Date())){
                return "Can't Cancel Booking! The show ended!";
            }
            if (booking.getUser().getId().equals(user.getId())) {
                booking.setStatus("Canceled");
                bookingRepo.save(booking);

                List<BookedSeat> bookedSeats = bookedSeatRepo.findAllByBooking_Id(bookingId);
                if (bookedSeats.size() < 1) {
                    throw new NotFoundException("seat not found");
                }
                for (BookedSeat bookedSeat : bookedSeats) {
                    bookedSeat.setBooked(false);
                    bookedSeatRepo.save(bookedSeat);
                }
            }
        }else {
            throw new NotFoundException("Booking not found");
        }
        return "Booking cancelled Successfully";
    }

    public BookingResponseDTO getBookingByTicketId(String tickectId) {
        Booking booking = bookingRepo.findByTicketId(tickectId);
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }

        BookingResponseDTO bookingResponseDTO = modelMapper.map(booking, BookingResponseDTO.class);
        bookingResponseDTO.setTheatreName(booking.getTheatre().getName());
        bookingResponseDTO.setShowId(booking.getShow().getShowId());
        bookingResponseDTO.setShowTitle(booking.getShow().getTitle());
        bookingResponseDTO.setShowTime(booking.getShow().getShowTime());
        bookingResponseDTO.setShowDate(booking.getShow().getShowDate());
        bookingResponseDTO.setLocation(booking.getShow().getLocation());

        List<BookedSeat> bookedSeats = bookedSeatRepo.findAllByBooking_Id(booking.getId());
        List<SeatBookingInfo> seatBookingInfos = new ArrayList<>();
        for (BookedSeat bookedSeat : bookedSeats) {
            SeatBookingInfo seatBookingInfo = new SeatBookingInfo();
            seatBookingInfo.setSeatIdentifier(bookedSeat.getSeatId());
            seatBookingInfo.setPrice(bookedSeat.getPrice());

            seatBookingInfos.add(seatBookingInfo);
        }

        bookingResponseDTO.setSeats(seatBookingInfos);
        return bookingResponseDTO;
    }

    public List<BookingResponseDTO> getAllBookingByUser(int userId) {
        List<Booking> bookings = bookingRepo.findAllByUserId(userId);
        List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingResponseDTO bookingResponseDTO = modelMapper.map(booking, BookingResponseDTO.class);
            bookingResponseDTO.setTheatreName(booking.getTheatre().getName());
            bookingResponseDTO.setShowId(booking.getShow().getShowId());

            List<BookedSeat> bookedSeats = bookedSeatRepo.findAllByBooking_Id(booking.getId());
            List<SeatBookingInfo> seatBookingInfos = new ArrayList<>();
            for (BookedSeat bookedSeat : bookedSeats) {
                SeatBookingInfo seatBookingInfo = new SeatBookingInfo();
                seatBookingInfo.setSeatIdentifier(bookedSeat.getSeatId());
                seatBookingInfo.setPrice(bookedSeat.getPrice());

                seatBookingInfos.add(seatBookingInfo);
            }

            bookingResponseDTO.setSeats(seatBookingInfos);
            bookingResponseDTOs.add(bookingResponseDTO);
        }
        return bookingResponseDTOs;
    }
}
