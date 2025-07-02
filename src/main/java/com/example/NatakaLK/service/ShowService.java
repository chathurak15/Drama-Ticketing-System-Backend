package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.SaveShowDTO;
import com.example.NatakaLK.dto.requestDTO.UpdateShowDTO;
import com.example.NatakaLK.dto.responseDTO.CityDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.dto.responseDTO.ShowResponseDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.City;
import com.example.NatakaLK.model.Drama;
import com.example.NatakaLK.model.Show;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.repo.DramaRepo;
import com.example.NatakaLK.repo.ShowRepo;
import com.example.NatakaLK.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShowService {
    @Autowired
    private ShowRepo showRepo;

    @Autowired
    private DramaRepo dramaRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String addShow(SaveShowDTO saveShowDTO) {
        Optional<User> userOpt = userRepo.findById(saveShowDTO.getUserId());
        Optional<Drama> dramaOpt = dramaRepo.findById(saveShowDTO.getDramaId());

        if (userOpt.isEmpty()) {
            return "User not found";
        }
        if (dramaOpt.isEmpty()) {
            return "Drama not found";
        }

        User user = userOpt.get();
        Drama drama = dramaOpt.get();
        if (!"approved".equalsIgnoreCase(user.getStatus())) {
            return "Your account is not approved";
        }

        Show newShow = new Show();
        newShow.setTitle(saveShowDTO.getTitle());
        newShow.setDescription(saveShowDTO.getDescription());
        newShow.setImage(saveShowDTO.getImage());
        newShow.setLocation(saveShowDTO.getLocation());
        newShow.setShowDate(saveShowDTO.getShowDate());
        newShow.setShowTime(saveShowDTO.getShowTime());
        newShow.setDrama(drama);
        newShow.setUser(user);
        newShow.setStatus("pending");

        showRepo.save(newShow);
        return "Show added successfully";
    }

//    public PaginatedDTO getAllByApproved(int page, int size, String title, String date, int id) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "showDate"));
//        Page<Show> shows = showRepo.findAllEqualsStatus(pageable, "approved");
//        if (shows.hasContent()){
//            List<ShowResponseDTO> showResponseDTOS = shows.getContent()
//                    .stream().map(d -> modelMapper.map(d, ShowResponseDTO.class))
//                    .collect(Collectors.toList());
//
//            return new PaginatedDTO(showResponseDTOS,shows.getTotalPages(),shows.getTotalElements());
//        }else{
//            throw new NotFoundException("dramas not found");
//        }
//    }

    public PaginatedDTO getAllByApproved(int page, int size, String title, String date, Integer cityId, String location) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "showDate"));

        LocalDate parsedDate = null;
        if (date != null && !date.isEmpty()) {
            parsedDate = LocalDate.parse(date);
        }

        Page<Show> shows = showRepo.findApprovedShowsWithFilters(pageable, title, parsedDate, cityId,location);

        if (shows.hasContent()) {
            List<ShowResponseDTO> dtos = shows.getContent().stream()
                    .map(show -> modelMapper.map(show, ShowResponseDTO.class))
                    .collect(Collectors.toList());
            return new PaginatedDTO(dtos, shows.getTotalPages(), shows.getTotalElements());
        } else {
            throw new NotFoundException("Shows not found");
        }
    }

    //this is admin only
    public PaginatedDTO getAll(int page, int size,String status) {
        Page<Show> shows;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "showDate"));

        if (status == null || status.trim().isEmpty() || status.equals("all")) {
            shows = showRepo.findAll(pageable);
        } else {
            // Validate input status
            List<String> validStatuses = List.of("approved", "pending", "rejected");
            if (!validStatuses.contains(status.toLowerCase())) {
                throw new NotFoundException("Invalid status: " + status);
            }
            shows = showRepo.findAllEqualsStatus(pageable, status);
        }
        if (shows.hasContent()){
            List<ShowResponseDTO> showResponseDTOS = shows.getContent()
                    .stream().map(d -> modelMapper.map(d, ShowResponseDTO.class))
                    .collect(Collectors.toList());

            return new PaginatedDTO(showResponseDTOS,shows.getTotalPages(),shows.getTotalElements());
        }else{
            throw new NotFoundException("dramas not found");
        }
    }

    public ShowResponseDTO getShowById(int id) {
        if (showRepo.existsById(id)) {
            return modelMapper.map(showRepo.findById(id).get(), ShowResponseDTO.class);
        }else {
            throw new NotFoundException("Show not found");
        }
    }

    public String deleteShow(int id, int userId) {
        if (showRepo.existsById(id)) {
            if(showRepo.findById(id).get().getUser().getId() == userId) {
                showRepo.deleteById(id);
                return "Show deleted successfully";
            }{
                throw new NotFoundException("User not allowed to delete this show");
            }
        }else{
            throw new NotFoundException("Show not found");
        }
    }

    public String updateShow(UpdateShowDTO updateShowDTO) {
        Show show = showRepo.findById(updateShowDTO.getShowId()).get();
        if (show != null && show.getUser().getId() == updateShowDTO.getUserId()) {
            show.setTitle(updateShowDTO.getTitle());
            show.setDescription(updateShowDTO.getDescription());
            show.setImage(updateShowDTO.getImage());
            show.setLocation(updateShowDTO.getLocation());
            show.setShowDate(updateShowDTO.getShowDate());
            show.setShowTime(updateShowDTO.getShowTime());
            show.setStatus("pending");

            Optional<Drama> drama = dramaRepo.findById(updateShowDTO.getDramaId());
            if (drama.isPresent()) {
                show.setDrama(drama.get());
                showRepo.save(show);
                return "Show updated successfully";
            }else {
                throw new NotFoundException("Drama not found");
            }
        }else{
            throw new NotFoundException("Updated failed. Try again!");
        }
    }

    public String updateShowStatus(int id, String status) {
        List<String> validStatuses = List.of("approved", "pending", "rejected");
        if (!validStatuses.contains(status.toLowerCase())) {
            return "Invalid status";
        }
        Optional<Show> showOpt = showRepo.findById(id);
        if (showOpt.isPresent()) {
            showOpt.get().setStatus(status);
            showRepo.save(showOpt.get());
            return "Show updated successfully";
        }else {
            throw new NotFoundException("Show not found");
        }
    }

    public List<String> getSortedUniqueLocationsByCityId(int cityId) {
        return showRepo.getDistinctLocationsByCity(cityId);
    }
}
