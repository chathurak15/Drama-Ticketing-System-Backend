package com.example.NatakaLK.service;


import com.example.NatakaLK.dto.requestDTO.DramaRequestDTO;
import com.example.NatakaLK.dto.requestDTO.DramaUpdateDTO;
import com.example.NatakaLK.dto.responseDTO.*;
import com.example.NatakaLK.exception.NotFoundException;
import com.example.NatakaLK.model.Actor;
import com.example.NatakaLK.model.Drama;
import com.example.NatakaLK.repo.ActorRepo;
import com.example.NatakaLK.repo.DramaRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DramaService {

    @Autowired
    private DramaRepo dramaRepo;

    @Autowired
    private ActorRepo actorRepo;

    @Autowired
    private ModelMapper modelMapper;

    //add drama
    public String addDrama(DramaRequestDTO dramaDTO) {
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

    //get all drama details with pagination(must be pass the size and page value)
    public PaginatedDTO getAll(int page, int size, String title, String sortByRating) {
            Sort sort = Sort.unsorted();

            if (sortByRating != null && !sortByRating.trim().isEmpty() && sortByRating.equalsIgnoreCase("desc")) {
                sort = Sort.by(Sort.Direction.DESC, "id");
            }

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Drama> dramaList;

            if (title == null || title.trim().isEmpty()) {
                dramaList = dramaRepo.findAll(pageable);
            } else {
                dramaList = dramaRepo.findByTitleContainingIgnoreCase(title, pageable);
            }

            if (dramaList.hasContent()) {
                List<DramasResponseDTO> dramasResponseDTOS = dramaList.stream()
                        .map(d -> modelMapper.map(d, DramasResponseDTO.class))
                        .collect(Collectors.toList());

                return new PaginatedDTO(dramasResponseDTOS, dramaList.getTotalPages(), dramaList.getTotalElements());
            } else {
                throw new NotFoundException("Dramas not found");
            }
    }

    //get drama with actor's details by drama id
    public DramaDTO getDramaById(int id) {
        if (dramaRepo.existsById(id)){
            Drama drama = dramaRepo.findById(id).get();
            List<Integer> actorIds = dramaRepo.getActorsId(id);
            List<Actor> actors = new ArrayList<>();
            for (int actorId : actorIds){
                Actor actor = actorRepo.findById(actorId).get();
                actors.add(actor);
            }
            List<ActorResponseDTO> actorResponseDTOS = actors.stream()
                    .map(actor -> modelMapper.map(actor, ActorResponseDTO.class)).collect(Collectors.toList());

            return new DramaDTO(
                    drama.getId(),
                    drama.getTitle(),
                    drama.getDescription(),
                    drama.getDuration(),
                    drama.getVideoUrl(),
                    drama.getImage(),
                    actorResponseDTOS);
        }else {
            throw new NotFoundException(id+"drama not found");
        }
    }

    //delete drama by drama id
    public String deleteDrama(int id) {
        if (dramaRepo.existsById(id)){
            dramaRepo.deleteById(id);
            return "Drama deleted successfully";
        }else{
            throw new NotFoundException("drama not found");
        }
    }

    //update drama
    public String updateDrama(DramaUpdateDTO dramaUpdateDTO) {
        Optional<Drama> optionalDrama = dramaRepo.findById(dramaUpdateDTO.getId());
        if (optionalDrama.isPresent()) {
            Drama existingDrama = optionalDrama.get();

            //delete Exit values
            dramaRepo.deleteDramaActorsByDramaId(existingDrama.getId());

            // Prepare new set of actors
            Set<Actor> newActors = new HashSet<>();
            for (int actorId : dramaUpdateDTO.getActorIds()) {
                actorRepo.findById(actorId).ifPresent(newActors::add);
            }
            modelMapper.map(dramaUpdateDTO, existingDrama);
            existingDrama.setActors(newActors);

            dramaRepo.save(existingDrama);
            return "Drama updated successfully";
        } else {
            throw new NotFoundException("Drama not found");
        }
    }

    public PaginatedDTO searchDramaByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (title == null || title.trim().isEmpty()) {
            throw new NotFoundException("Drama title must not be empty");
        }
        // Perform a case insensitive search for dramas whose title contains the search keyword
        Page<Drama> dramaList = dramaRepo.findByTitleContainingIgnoreCase(title, pageable);
        if (!dramaList.hasContent()) {
            throw new NotFoundException("No dramas found for title: " + title);
        }
        // Convert the list of Drama entities to a list of DramasResponseDTO using ModelMapper
        List<DramasResponseDTO> dramasResponseDTOS = dramaList.getContent()
                .stream()
                .map(d -> modelMapper.map(d, DramasResponseDTO.class))
                .collect(Collectors.toList());

        return new PaginatedDTO(dramasResponseDTOS, dramaList.getTotalPages(), dramaList.getTotalElements());
    }
}
