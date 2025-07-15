package com.example.NatakaLK.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalUsers;
    private long activeEvents;
    private long pendingApprovals;
    private double totalRevenue;
    private double monthlyGrowth;
}
