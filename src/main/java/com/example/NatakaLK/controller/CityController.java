package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.responseDTO.CityDTO;
import com.example.NatakaLK.model.City;
import com.example.NatakaLK.repo.CityRepo;
import com.example.NatakaLK.service.ShowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/city")
@CrossOrigin
public class CityController {
    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private ShowService showService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity<List<CityDTO>> getCity() {
        List<City> city = cityRepo.findAll();
        List<CityDTO> cityDTOs = city.stream().map(city1 -> modelMapper.map(city1, CityDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(cityDTOs);
    }

    @GetMapping("/venue/{id}")
    public ResponseEntity<List<String>> getVenuesByCityId(@PathVariable int id) {
        if (!cityRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<String> locations = showService.getSortedUniqueLocationsByCityId(id);
        return ResponseEntity.ok(locations);
    }
}
