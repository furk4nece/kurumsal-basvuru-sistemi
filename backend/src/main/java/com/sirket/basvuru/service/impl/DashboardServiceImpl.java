package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.dto.response.DashboardResponse;
import com.sirket.basvuru.enums.ApplicationStatus;
import com.sirket.basvuru.enums.Role;
import com.sirket.basvuru.mapper.ApplicationFormMapper;
import com.sirket.basvuru.repository.ApplicationFormRepository;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ApplicationFormRepository applicationFormRepository;
    private final UserRepository userRepository;
    private final ApplicationFormMapper applicationFormMapper;

    @Override
    public DashboardResponse getSummary(String requesterEmail) {
        boolean admin = userRepository.findByEmail(requesterEmail)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);

        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        LocalDateTime dayEnd = LocalDate.now().atTime(LocalTime.MAX);

        long total = admin
                ? applicationFormRepository.count()
                : applicationFormRepository.countByApplicant_Email(requesterEmail);

        long newCount = count(ApplicationStatus.NEW, admin, requesterEmail);
        long inReviewCount = count(ApplicationStatus.IN_REVIEW, admin, requesterEmail);

        long todayCount = admin
                ? applicationFormRepository.countByCreatedDateBetween(dayStart, dayEnd)
                : applicationFormRepository.countByCreatedDateBetweenAndApplicant_Email(dayStart, dayEnd, requesterEmail);

        List<ApplicationFormResponse> recent = (admin
                ? applicationFormRepository.findTop10ByOrderByCreatedDateDesc()
                : applicationFormRepository.findTop10ByApplicant_EmailOrderByCreatedDateDesc(requesterEmail))
                .stream()
                .map(applicationFormMapper::toResponse)
                .toList();

        return DashboardResponse.builder()
                .totalCount(total)
                .newCount(newCount)
                .inReviewCount(inReviewCount)
                .pendingCount(newCount + inReviewCount)
                .approvedCount(count(ApplicationStatus.APPROVED, admin, requesterEmail))
                .rejectedCount(count(ApplicationStatus.REJECTED, admin, requesterEmail))
                .cancelledCount(count(ApplicationStatus.CANCELLED, admin, requesterEmail))
                .todayCount(todayCount)
                .recentApplications(recent)
                .build();
    }

    private long count(ApplicationStatus status, boolean admin, String email) {
        return admin
                ? applicationFormRepository.countByStatus(status)
                : applicationFormRepository.countByStatusAndApplicant_Email(status, email);
    }
}