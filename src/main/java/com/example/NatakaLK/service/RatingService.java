package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.RatingDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.dto.responseDTO.RatingResponseDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.Drama;
import com.example.NatakaLK.model.Rating;
import com.example.NatakaLK.model.User;
import com.example.NatakaLK.repo.DramaRepo;
import com.example.NatakaLK.repo.RatingRepo;
import com.example.NatakaLK.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DramaRepo dramaRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String submitRating(RatingDTO ratingDTO) {

        if(ratingRepo.findByUserIdAndDramaId(ratingDTO.getUserId(), ratingDTO.getDramaId()).isPresent()){
            return "Rating Already Exists";
        }

        Drama drama =  dramaRepo.findById(ratingDTO.getDramaId()).orElseThrow(() -> new NotFoundException("Drama not found"));

        User user = userRepo.findById(ratingDTO.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

        Rating rating = new Rating();
        rating.setRatingValue(ratingDTO.getRatingValue());
        rating.setDrama(drama);
        rating.setUser(user);
        rating.setComment(ratingDTO.getComment());
        ratingRepo.save(rating);
        return "Rating saved";
    }

    public Double getAverageRating(int dramaId) {
        return ratingRepo.findAverageRatingByDramaId(dramaId);
    }

    public PaginatedDTO getAllRatingsByDrama(int page, int size, int dramaId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<Rating> ratings = ratingRepo.findAllByDramaId(pageable, dramaId);
        if (ratings.isEmpty()) {
            throw new NotFoundException("Rating not found");
        }
        List<RatingResponseDTO> ratingResponseDTOS = ratings.getContent()
                .stream().map(d -> modelMapper.map(d, RatingResponseDTO.class))
                .collect(Collectors.toList());

        return new PaginatedDTO(ratingResponseDTOS, ratings.getTotalPages(), ratings.getTotalElements());
    }
}
