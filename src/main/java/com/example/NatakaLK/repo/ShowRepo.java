package com.example.NatakaLK.repo;

import com.example.NatakaLK.model.City;
import com.example.NatakaLK.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShowRepo extends JpaRepository<Show, Integer> {

    @Query("SELECT s FROM Show s WHERE LOWER(s.status) = LOWER(:status)")
    Page<Show> findAllEqualsStatus(Pageable pageable, @Param("status") String status);

    @Query("SELECT s FROM Show s " +
            "WHERE s.status = 'approved' " +
            "AND (:title IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:date IS NULL OR s.showDate = :date) " +
            "AND (:cityId IS NULL OR s.city.id = :cityId) " +
            "AND (:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:dramaId IS NULL OR s.drama.id = :dramaId)")
    Page<Show> findApprovedShowsWithFilters(
            Pageable pageable,
            @Param("title") String title,
            @Param("date") LocalDate date,
            @Param("cityId") Integer cityId,
            @Param("location") String location,
            @Param("dramaId") Integer dramaId);


    @Query("SELECT DISTINCT s.location FROM Show s WHERE s.city.id = :cityId ORDER BY s.showDate ASC")
    List<String> getDistinctLocationsByCity(@Param("cityId") int cityId);

    @Query("SELECT s FROM Show s " +
            "WHERE s.user.id = :userId " +
            "AND (:title IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:status IS NULL OR LOWER(s.status) = LOWER(:status))")
    Page<Show> findUserShowsWithFilters(
            Pageable pageable,
            @Param("title") String title,
            @Param("status") String status,
            @Param("userId") Integer userId);

    Page<Show> findAllByDramaId(Pageable pageable, int dramaId);

    List<Show> findAllByUserId(int userId);

    // Upcoming shows without filters
    @Query("SELECT s FROM Show s WHERE s.showDate >= CURRENT_DATE AND s.status = 'approved' ORDER BY s.showDate ASC")
    List<Show> findUpcomingShows();

    // Upcoming shows by city name
    @Query("SELECT s FROM Show s WHERE s.city.cityName LIKE %:cityName% AND s.showDate >= CURRENT_DATE AND s.status = 'approved' ORDER BY s.showDate ASC")
    List<Show> findUpcomingShowsByCity(@Param("cityName") String cityName);

    // Shows by drama title
    @Query("SELECT s FROM Show s WHERE LOWER(s.drama.title) LIKE LOWER(CONCAT('%', :dramaTitle, '%')) AND s.status = 'approved'")
    List<Show> findShowsByDramaTitle(@Param("dramaTitle") String dramaTitle);

    long countByStatus(String status);
}
