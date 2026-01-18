package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.responseDTO.DashboardStatsDTO;
import com.example.NatakaLK.repo.BookingRepo;
import com.example.NatakaLK.repo.ShowRepo;
import com.example.NatakaLK.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dashboard")
@CrossOrigin
public class AdminDashboardController {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ShowRepo showRepository;

    @Autowired
    private BookingRepo bookingRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('Admin')")
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalUsers(userRepository.count());

        stats.setActiveEvents(showRepository.countByStatus("approved"));
        stats.setPendingApprovals(showRepository.countByStatus("pending"));

        stats.setTotalRevenue(bookingRepository.getTotalRevenue());
        stats.setMonthlyGrowth(12.5); // You can calculate this based on bookings

        return stats;
    }
}
