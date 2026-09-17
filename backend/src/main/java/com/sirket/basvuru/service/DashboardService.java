package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getSummary(String requesterEmail);
}