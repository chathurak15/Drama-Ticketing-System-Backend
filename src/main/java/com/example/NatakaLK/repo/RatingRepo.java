package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByUserIdAndDramaId(int userId, int dramaId);

    Page findAllByDramaId(Pageable pageable, int dramaId);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.drama.id = :dramaId")
    Double findAverageRatingByDramaId(@Param("dramaId") int dramaId);

    Optional<Rating> findByDramaId(int dramaId);
}
