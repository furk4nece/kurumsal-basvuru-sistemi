package com.sirket.basvuru.repository;

import com.sirket.basvuru.entity.ApplicationForm;
import com.sirket.basvuru.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long>,
        JpaSpecificationExecutor<ApplicationForm> {

    long countByStatus(ApplicationStatus status);

    long countByApplicant_Email(String email);

    long countByStatusAndApplicant_Email(ApplicationStatus status, String email);

    long countByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    long countByCreatedDateBetweenAndApplicant_Email(LocalDateTime start, LocalDateTime end, String email);

    List<ApplicationForm> findTop10ByOrderByCreatedDateDesc();

    List<ApplicationForm> findTop10ByApplicant_EmailOrderByCreatedDateDesc(String email);

    boolean existsByApplicant_Id(Long applicantId);
}