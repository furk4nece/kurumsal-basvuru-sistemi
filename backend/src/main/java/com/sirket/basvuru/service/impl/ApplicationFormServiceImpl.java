package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.enums.ApplicationStatus;
import com.sirket.basvuru.dto.request.ApplicationFormRequest;
import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.entity.ApplicationForm;
import com.sirket.basvuru.entity.FormType;
import com.sirket.basvuru.entity.User;
import com.sirket.basvuru.mapper.ApplicationFormMapper;
import com.sirket.basvuru.repository.ApplicationFormRepository;
import com.sirket.basvuru.repository.FormTypeRepository;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.service.ApplicationFormService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationFormServiceImpl implements ApplicationFormService {

    private final ApplicationFormRepository applicationFormRepository;
    private final FormTypeRepository formTypeRepository;
    private final UserRepository userRepository;
    private final ApplicationFormMapper applicationFormMapper;

    @Override
    public ApplicationFormResponse create(ApplicationFormRequest request, String applicantEmail) {
        FormType formType = formTypeRepository.findById(request.getFormTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Form turu bulunamadi: id=" + request.getFormTypeId()));

        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new EntityNotFoundException("Kullanici bulunamadi: " + applicantEmail));

        ApplicationForm applicationForm = ApplicationForm.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .formType(formType)
                .applicant(applicant)
                .build();
        ApplicationForm saved = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toResponse(saved);
    }

    @Override
    public List<ApplicationFormResponse> getAll() {
        return applicationFormRepository.findAll()
                .stream()
                .map(applicationFormMapper::toResponse)
                .toList();
    }

    @Override
    public ApplicationFormResponse getById(Long id) {
        return applicationFormMapper.toResponse(findEntityById(id));
    }

    @Override
    public ApplicationFormResponse update(Long id, ApplicationFormRequest request, String requesterEmail) {
        ApplicationForm applicationForm = findEntityById(id);

        checkOwnership(applicationForm, requesterEmail);

        FormType formType = formTypeRepository.findById(request.getFormTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Form turu bulunamadi: id=" + request.getFormTypeId()));

        applicationForm.setTitle(request.getTitle());
        applicationForm.setDescription(request.getDescription());
        applicationForm.setFormType(formType);

        ApplicationForm updated = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id, String requesterEmail) {
        ApplicationForm applicationForm = findEntityById(id);
        checkOwnership(applicationForm, requesterEmail);
        applicationFormRepository.delete(applicationForm);
    }

    private ApplicationForm findEntityById(Long id) {
        return applicationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Basvuru bulunamadi: id=" + id));
    }

    private void checkOwnership(ApplicationForm applicationForm, String requesterEmail) {
        if (!applicationForm.getApplicant().getEmail().equals(requesterEmail)) {
            throw new AccessDeniedException("Bu basvuruyu degistirme yetkiniz yok");
        }
    }

        @Override
    public ApplicationFormResponse approve(Long id) {
        ApplicationForm applicationForm = findEntityById(id);
        validateStatusTransition(applicationForm);
        applicationForm.setStatus(ApplicationStatus.APPROVED);
        ApplicationForm updated = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toResponse(updated);
    }

    @Override
    public ApplicationFormResponse reject(Long id) {
        ApplicationForm applicationForm = findEntityById(id);
        validateStatusTransition(applicationForm);
        applicationForm.setStatus(ApplicationStatus.REJECTED);
        ApplicationForm updated = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toResponse(updated);
    }

    private void validateStatusTransition(ApplicationForm applicationForm) {
        ApplicationStatus current = applicationForm.getStatus();
        if (current != ApplicationStatus.NEW && current != ApplicationStatus.IN_REVIEW) {
            throw new IllegalStateException(
                    "Bu basvuru zaten sonuclanmis (durum: " + current + "), tekrar islem yapilamaz");
        }
    }
}

    

