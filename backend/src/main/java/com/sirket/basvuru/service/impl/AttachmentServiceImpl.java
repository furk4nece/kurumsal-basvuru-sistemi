package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import com.sirket.basvuru.entity.ApplicationForm;
import com.sirket.basvuru.entity.Attachment;
import com.sirket.basvuru.enums.Role;
import com.sirket.basvuru.mapper.AttachmentMapper;
import com.sirket.basvuru.repository.ApplicationFormRepository;
import com.sirket.basvuru.repository.AttachmentRepository;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.service.AttachmentService;
import com.sirket.basvuru.service.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final ApplicationFormRepository applicationFormRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final AttachmentMapper attachmentMapper;

    @Override
    public AttachmentResponse upload(Long applicationFormId, MultipartFile file, String requesterEmail) {
        ApplicationForm applicationForm = applicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new EntityNotFoundException("Basvuru bulunamadi: id=" + applicationFormId));

        checkAccess(applicationForm, requesterEmail);

        String generatedFileName = fileStorageService.store(file);

        Attachment attachment = Attachment.builder()
                .fileName(generatedFileName)
                .originalName(file.getOriginalFilename())
                .filePath(generatedFileName)
                .applicationForm(applicationForm)
                .build();

        return attachmentMapper.toResponse(attachmentRepository.save(attachment));
    }

    @Override
    public void delete(Long attachmentId, String requesterEmail) {
        Attachment attachment = findById(attachmentId);
        checkAccess(attachment.getApplicationForm(), requesterEmail);
        attachmentRepository.delete(attachment);
        fileStorageService.delete(attachment.getFileName());
    }

    @Override
    @Transactional(readOnly = true)
    public Attachment getForDownload(Long attachmentId, String requesterEmail) {
        Attachment attachment = findById(attachmentId);
        checkAccess(attachment.getApplicationForm(), requesterEmail);
        return attachment;
    }

    private Attachment findById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("Ek dosya bulunamadi: id=" + attachmentId));
    }

    private void checkAccess(ApplicationForm applicationForm, String requesterEmail) {
        boolean owner = applicationForm.getApplicant().getEmail().equals(requesterEmail);
        boolean admin = userRepository.findByEmail(requesterEmail)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);

        if (!owner && !admin) {
            throw new AccessDeniedException("Bu dosya uzerinde islem yapma yetkiniz yok");
        }
    }
}