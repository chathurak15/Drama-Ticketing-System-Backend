package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Drama;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DramaRepo extends JpaRepository<Drama, Integer> {
    boolean existsByTitle(String title);

    @Query(value = "SELECT actor_id FROM drama_actor WHERE drama_id =:id" , nativeQuery = true)
    List<Integer> getActorsId(int id);

    List<Drama> findByTitle(String title);

    Page<Drama> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
