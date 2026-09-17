package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.request.ApplicationFormRequest;
import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ApplicationFormService {

    ApplicationFormResponse create(ApplicationFormRequest request, String requesterEmail);

    Page<ApplicationFormResponse> getAll(ApplicationStatus status,
                                         Long formTypeId,
                                         String keyword,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         Pageable pageable,
                                         String requesterEmail);

    ApplicationFormResponse getById(Long id, String requesterEmail);

    ApplicationFormResponse update(Long id, ApplicationFormRequest request, String requesterEmail);

    void delete(Long id, String requesterEmail);

    ApplicationFormResponse approve(Long id);

    ApplicationFormResponse reject(Long id);

    ApplicationFormResponse changeStatus(Long id, ApplicationStatus status);

    ApplicationFormResponse cancel(Long id, String requesterEmail);
}