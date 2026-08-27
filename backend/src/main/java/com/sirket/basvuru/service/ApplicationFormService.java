package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.request.ApplicationFormRequest;
import com.sirket.basvuru.dto.response.ApplicationFormResponse;

import java.util.List;

public interface ApplicationFormService {
    ApplicationFormResponse create(ApplicationFormRequest request, String applicantEmail);
    List<ApplicationFormResponse> getAll();
    ApplicationFormResponse getById(Long id);
    ApplicationFormResponse update(Long id, ApplicationFormRequest request, String requesterEmail);
    void delete(Long id, String requesterEmail);

    ApplicationFormResponse approve(Long id);
    ApplicationFormResponse reject(Long id);
}