package com.sirket.basvuru.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DashboardResponse {
    private long totalCount;
    private long newCount;
    private long inReviewCount;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;
    private long cancelledCount;
    private long todayCount;
    private List<ApplicationFormResponse> recentApplications;
}