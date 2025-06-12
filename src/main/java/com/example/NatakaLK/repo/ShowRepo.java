package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowRepo extends JpaRepository<Show, Integer> {

    @Query("SELECT s FROM Show s WHERE LOWER(s.status) = LOWER(:status)")
    Page<Show> findAllEqualsStatus(Pageable pageable, @Param("status") String status);
}
