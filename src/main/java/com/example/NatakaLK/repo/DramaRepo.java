package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Drama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepo extends JpaRepository<Drama, Integer> {
    boolean findByTitle(String title);

    boolean existsByTitle(String title);
}
