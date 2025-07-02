package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Actor;
import com.example.NatakaLK.model.Drama;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DramaRepo extends JpaRepository<Drama, Integer> {
    boolean existsByTitle(String title);

    @Query(value = "SELECT actor_id FROM drama_actor WHERE drama_id =:id" , nativeQuery = true)
    List<Integer> getActorsId(int id);

    List<Drama> findByTitle(String title);

    Page<Drama> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM drama_actor WHERE drama_id = :dramaId", nativeQuery = true)
    void deleteDramaActorsByDramaId(int dramaId);

    boolean existsByActors(Actor actor);
}
