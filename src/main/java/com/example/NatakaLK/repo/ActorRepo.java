package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepo extends JpaRepository<Actor, Integer> {

    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
