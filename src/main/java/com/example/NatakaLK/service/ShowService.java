package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.SaveShowDTO;
import com.example.NatakaLK.dto.responseDTO.DramasResponseDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.dto.responseDTO.ShowResponseDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.Drama;
import com.example.NatakaLK.model.Show;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.repo.DramaRepo;
import com.example.NatakaLK.repo.ShowRepo;
import com.example.NatakaLK.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public PaginatedDTO getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "showId"));
        Page<Show> shows = showRepo.findAll(pageable);
        if (shows.hasContent()){
            List<ShowResponseDTO> showResponseDTOS = shows.getContent()
                    .stream().map(d -> modelMapper.map(d, ShowResponseDTO.class))
                    .collect(Collectors.toList());

            return new PaginatedDTO(showResponseDTOS,shows.getTotalPages(),shows.getTotalElements());
        }else{
            throw new NotFoundException("dramas not found");
        }
    }
}
