package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.requestDTO.ActorDTO;
import com.example.NatakaLK.dto.responseDTO.ActorResponseDTO;
import com.example.NatakaLK.dto.responseDTO.PaginatedDTO;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.Actor;
import com.example.NatakaLK.repo.ActorRepo;
import com.example.NatakaLK.repo.DramaRepo;
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
public class ActorService {
    @Autowired
    private ActorRepo actorRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DramaRepo dramaRepo;

    public String addActor(ActorDTO actorDTO) {
        try {
            Actor actor = modelMapper.map(actorDTO, Actor.class);
            actorRepo.save(actor);
            return "Actor added successfully";
        }catch (Exception e){
            return "Error adding actor" + e.getMessage();
        }
    }

    public PaginatedDTO getAllActors(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Actor> actors;
        if (name == null || name.equals("")) {
            actors = actorRepo.findAll(pageable);
        }else{
            actors = actorRepo.findByNameContainingIgnoreCase(name,pageable);
        }
        if (actors.hasContent()){
            List<ActorResponseDTO> actorResponseDTOS = actors.getContent()
                    .stream().map(a -> modelMapper.map(a, ActorResponseDTO.class))
                    .collect(Collectors.toList());

            return new PaginatedDTO(actorResponseDTOS,actors.getTotalPages(),actors.getTotalElements());
        }else{
            throw new NotFoundException("dramas not found");
        }
    }

    public String deleteActor(int id) {
        Optional<Actor> optionalActor = actorRepo.findById(id);

        if (optionalActor.isEmpty()) {
            throw new NotFoundException("Actor not found");
        }

        Actor actor = optionalActor.get();

        if (dramaRepo.existsByActors(actor)) {
            return "Actor cannot be deleted! Actor is linked to dramas";
        }

        actorRepo.deleteById(id);
        return "Actor deleted successfully";
    }
}
