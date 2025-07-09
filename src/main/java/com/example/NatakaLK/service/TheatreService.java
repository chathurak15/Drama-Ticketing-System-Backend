package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.SeatTypeDTO;
import com.example.NatakaLK.dto.requestDTO.TheatreDTO;
import com.example.NatakaLK.dto.responseDTO.SeatTypeResponseDTO;
import com.example.NatakaLK.dto.responseDTO.TheatreResponseDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.SeatType;
import com.example.NatakaLK.model.Show;
import com.example.NatakaLK.model.Theatre;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.model.enums.AreaType;
import com.example.NatakaLK.model.enums.TheatreStatus;
import com.example.NatakaLK.repo.SeatTypeRepo;
import com.example.NatakaLK.repo.ShowRepo;
import com.example.NatakaLK.repo.TheatreRepo;
import com.example.NatakaLK.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TheatreService {

    @Autowired
    private TheatreRepo theatreRepo;

    @Autowired
    private SeatTypeRepo seatTypeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShowRepo showRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<TheatreResponseDTO> getTheatersByManager(int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Theatre> theatres = theatreRepo.findByCreatedBy(user);
        List<TheatreResponseDTO> theatreResponseDTOs = theatres.stream()
                .map(theatre -> modelMapper.map(theatre, TheatreResponseDTO.class))
                .collect(Collectors.toList());

        return theatreResponseDTOs;
    }

    public String createPermanentTheatre(TheatreDTO theatreDTO, int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Theatre theatre = new Theatre();
        theatre.setName(theatreDTO.getName());
        theatre.setAreaType(theatreDTO.getAreaType());
        theatre.setStatus(TheatreStatus.PERMANENT);
        theatre.setCreatedBy(user);

        if (theatreDTO.getAreaType() == AreaType.OPEN) {
            theatre.setTotalCapacity(theatreDTO.getTotalCapacity());
            theatre.setOpenAreaPrice(theatreDTO.getOpenAreaPrice());
        }

        theatre = theatreRepo.save(theatre);

        // Create seat types if it's a theater
        if (theatreDTO.getAreaType() == AreaType.THEATRE && theatreDTO.getSeatTypes() != null) {
            for (SeatTypeDTO seatTypeReq : theatreDTO.getSeatTypes()) {
                SeatType seatType = new SeatType();
                seatType.setTheatre(theatre);
                seatType.setTypeName(seatTypeReq.getTypeName());
                seatType.setTotalSeats(seatTypeReq.getTotalSeats());
                seatType.setSeatsPerRow(seatTypeReq.getSeatsPerRow());
                seatTypeRepo.save(seatType);
            }
        }
//        theatreRepo.findById(theatre.getId()).get();

        return "Theatre Added Successfully!";
    }

    public TheatreResponseDTO createTemporaryTheatre(TheatreDTO theatreDTO, int organizerId, int showId) {
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Show show = showRepo.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        Theatre theatre = new Theatre();
        theatre.setName(theatreDTO.getName());
        theatre.setAreaType(theatreDTO.getAreaType());
        theatre.setStatus(TheatreStatus.TEMPORARY);
        theatre.setCreatedBy(organizer);
        theatre.setShow(show);

        if (theatreDTO.getAreaType() == AreaType.OPEN) {
            theatre.setTotalCapacity(theatreDTO.getTotalCapacity());
            theatre.setOpenAreaPrice(theatreDTO.getOpenAreaPrice());
        }

        theatre = theatreRepo.save(theatre);

        if (theatreDTO.getAreaType() == AreaType.THEATRE && theatreDTO.getSeatTypes() != null) {
            for (SeatTypeDTO seatTypeReq : theatreDTO.getSeatTypes()) {
                SeatType seatType = new SeatType();
                seatType.setTheatre(theatre);
                seatType.setTypeName(seatTypeReq.getTypeName());
                seatType.setTotalSeats(seatTypeReq.getTotalSeats());
                seatType.setSeatsPerRow(seatTypeReq.getSeatsPerRow());
                System.out.println("Theatre for SeatType: " + seatType.getTheatre());
                seatTypeRepo.save(seatType);
            }
        }

        theatre = theatreRepo.findById(theatre.getId()).orElseThrow();
        return modelMapper.map(theatre, TheatreResponseDTO.class);
    }

    public List<SeatTypeResponseDTO> getSeatTypesByTheatreId(long id) {
        if (theatreRepo.findById(id).isEmpty()){
            return null;
        }
        List<SeatType> seatTypes = seatTypeRepo.findByTheatreId(id);
        List<SeatTypeResponseDTO> seatTypeResponseDTOs = seatTypes.stream()
                .map(seatType -> modelMapper.map (seatType, SeatTypeResponseDTO.class))
                .collect(Collectors.toList());

        return seatTypeResponseDTOs;
    }

    public String deleteTheatre(long theatreId, int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Theatre theatre = theatreRepo.findById(theatreId).orElseThrow(() -> new RuntimeException("Theatre not found"));

        if (theatre.getCreatedBy() != user) {
            return "Theatre Deleted Failed! Try Again!";
        }
        theatreRepo.delete(theatre);
        return "Theatre Deleted Successfully!";
    }
}
