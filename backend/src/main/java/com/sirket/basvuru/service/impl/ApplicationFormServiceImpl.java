package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.request.ApplicationFormRequest;
import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.entity.ApplicationForm;
import com.sirket.basvuru.entity.FormType;
import com.sirket.basvuru.entity.User;
import com.sirket.basvuru.enums.ApplicationStatus;
import com.sirket.basvuru.enums.Role;
import com.sirket.basvuru.mapper.ApplicationFormMapper;
import com.sirket.basvuru.repository.ApplicationFormRepository;
import com.sirket.basvuru.repository.FormTypeRepository;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.service.ApplicationFormService;
import com.sirket.basvuru.specification.ApplicationFormSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationFormServiceImpl implements ApplicationFormService {

    private final ApplicationFormRepository applicationFormRepository;
    private final FormTypeRepository formTypeRepository;
    private final UserRepository userRepository;
    private final ApplicationFormMapper applicationFormMapper;

    @Override
    public ApplicationFormResponse create(ApplicationFormRequest request, String requesterEmail) {
        FormType formType = findFormType(request.getFormTypeId());
        User applicant = findUser(requesterEmail);

        ApplicationForm applicationForm = ApplicationForm.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .formType(formType)
                .applicant(applicant)
                .status(ApplicationStatus.NEW)
                .build();

        return applicationFormMapper.toResponse(applicationFormRepository.save(applicationForm));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationFormResponse> getAll(ApplicationStatus status,
                                                Long formTypeId,
                                                String keyword,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                Pageable pageable,
                                                String requesterEmail) {

        String ownerFilter = isAdmin(requesterEmail) ? null : requesterEmail;

        Specification<ApplicationForm> spec = Specification
                .where(ApplicationFormSpecification.belongsTo(ownerFilter))
                .and(ApplicationFormSpecification.hasStatus(status))
                .and(ApplicationFormSpecification.hasFormType(formTypeId))
                .and(ApplicationFormSpecification.keywordContains(keyword))
                .and(ApplicationFormSpecification.createdAfter(startDate == null ? null : startDate.atStartOfDay()))
                .and(ApplicationFormSpecification.createdBefore(endDate == null ? null : endDate.atTime(LocalTime.MAX)));

        return applicationFormRepository.findAll(spec, pageable).map(applicationFormMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationFormResponse getById(Long id, String requesterEmail) {
        ApplicationForm applicationForm = findEntityById(id);
        checkAccess(applicationForm, requesterEmail);
        return applicationFormMapper.toResponse(applicationForm);
    }

    @Override
    public ApplicationFormResponse update(Long id, ApplicationFormRequest request, String requesterEmail) {
        ApplicationForm applicationForm = findEntityById(id);
        checkAccess(applicationForm, requesterEmail);

        if (applicationForm.getStatus() != ApplicationStatus.NEW
                && applicationForm.getStatus() != ApplicationStatus.IN_REVIEW) {
            throw new IllegalStateException("Sonuclanmis basvuru guncellenemez (durum: "
                    + applicationForm.getStatus() + ")");
        }

        applicationForm.setTitle(request.getTitle());
        applicationForm.setDescription(request.getDescription());
        applicationForm.setFormType(findFormType(request.getFormTypeId()));

        return applicationFormMapper.toResponse(applicationFormRepository.save(applicationForm));
    }

    @Override
    public void delete(Long id, String requesterEmail) {
        ApplicationForm applicationForm = findEntityById(id);
        checkAccess(applicationForm, requesterEmail);
        applicationFormRepository.delete(applicationForm);
    }

    @Override
    public ApplicationFormResponse approve(Long id) {
        return changeStatus(id, ApplicationStatus.APPROVED);
    }

    @Override
    public ApplicationFormResponse reject(Long id) {
        return changeStatus(id, ApplicationStatus.REJECTED);
    }

    @Override
    public ApplicationFormResponse changeStatus(Long id, ApplicationStatus status) {
        ApplicationForm applicationForm = findEntityById(id);
        validateTransition(applicationForm.getStatus(), status);
        applicationForm.setStatus(status);
        return applicationFormMapper.toResponse(applicationFormRepository.save(applicationForm));
    }

    @Override
    public ApplicationFormResponse cancel(Long id, String requesterEmail) {
        ApplicationForm applicationForm = findEntityById(id);
        checkAccess(applicationForm, requesterEmail);
        validateTransition(applicationForm.getStatus(), ApplicationStatus.CANCELLED);
        applicationForm.setStatus(ApplicationStatus.CANCELLED);
        return applicationFormMapper.toResponse(applicationFormRepository.save(applicationForm));
    }

    private void validateTransition(ApplicationStatus current, ApplicationStatus target) {
        if (current == target) {
            throw new IllegalStateException("Basvuru zaten " + current + " durumunda");
        }
        boolean open = current == ApplicationStatus.NEW || current == ApplicationStatus.IN_REVIEW;
        if (!open) {
            throw new IllegalStateException("Bu basvuru zaten sonuclanmis (durum: " + current
                    + "), tekrar islem yapilamaz");
        }
        if (target == ApplicationStatus.NEW) {
            throw new IllegalStateException("Basvuru NEW durumuna geri alinamaz");
        }
    }

    private ApplicationForm findEntityById(Long id) {
        return applicationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Basvuru bulunamadi: id=" + id));
    }

    private FormType findFormType(Long formTypeId) {
        return formTypeRepository.findById(formTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Form turu bulunamadi: id=" + formTypeId));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Kullanici bulunamadi: " + email));
    }

    private boolean isAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);
    }

    private void checkAccess(ApplicationForm applicationForm, String requesterEmail) {
        boolean owner = applicationForm.getApplicant().getEmail().equals(requesterEmail);
        if (!owner && !isAdmin(requesterEmail)) {
            throw new AccessDeniedException("Bu basvuru uzerinde islem yapma yetkiniz yok");
        }
    }
}