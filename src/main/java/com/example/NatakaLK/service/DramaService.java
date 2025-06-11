package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.ActorDTO;
import com.example.NatakaLK.dto.requestDTO.DramaDTO;
import com.example.NatakaLK.model.Actor;
import com.example.NatakaLK.model.Drama;
import com.example.NatakaLK.repo.ActorRepo;
import com.example.NatakaLK.repo.DramaRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DramaService {

    @Autowired
    private DramaRepo dramaRepo;

    @Autowired
    private ActorRepo actorRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String addDrama(DramaDTO dramaDTO) {
        if (dramaDTO ==null) {
            return "Drama details was missing";
        }
        if (dramaRepo.existsByTitle(dramaDTO.getTitle())){
            return "Drama already exists";
        }
        // Fetch actors by ID
        Set<Actor> actors = new HashSet<>();
        for (int actorId : dramaDTO.getActorIds()) {
            Optional<Actor> optionalActor = actorRepo.findById(actorId);
            if (optionalActor.isPresent()) {
                actors.add(optionalActor.get());
            }
        }
        Drama drama = modelMapper.map(dramaDTO, Drama.class);
        drama.setActors(actors);
        dramaRepo.save(drama);
        return "Drama Added";
    }
}
