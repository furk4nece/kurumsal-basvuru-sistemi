package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import com.sirket.basvuru.entity.ApplicationForm;
import com.sirket.basvuru.entity.Attachment;
import com.sirket.basvuru.mapper.AttachmentMapper;
import com.sirket.basvuru.repository.ApplicationFormRepository;
import com.sirket.basvuru.repository.AttachmentRepository;
import com.sirket.basvuru.service.AttachmentService;
import com.sirket.basvuru.service.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final ApplicationFormRepository applicationFormRepository;
    private final FileStorageService fileStorageService;
    private final AttachmentMapper attachmentMapper;

    @Override
    public AttachmentResponse upload(Long applicationFormId, MultipartFile file, String requesterEmail) {
        ApplicationForm applicationForm = applicationFormRepository.findById(applicationFormId)
                .orElseThrow(() -> new EntityNotFoundException("Basvuru bulunamadi: id=" + applicationFormId));

        if (!applicationForm.getApplicant().getEmail().equals(requesterEmail)) {
            throw new AccessDeniedException("Bu basvuruya dosya ekleme yetkiniz yok");
        }

        String generatedFileName = fileStorageService.store(file);

        Attachment attachment = Attachment.builder()
                .fileName(generatedFileName)
                .originalName(file.getOriginalFilename())
                .filePath(generatedFileName)
                .applicationForm(applicationForm)
                .build();

        Attachment saved = attachmentRepository.save(attachment);
        return attachmentMapper.toResponse(saved);
    }

    @Override
    public void delete(Long attachmentId, String requesterEmail) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("Ek dosya bulunamadi: id=" + attachmentId));

        if (!attachment.getApplicationForm().getApplicant().getEmail().equals(requesterEmail)) {
            throw new AccessDeniedException("Bu dosyayi silme yetkiniz yok");
        }

        attachmentRepository.delete(attachment);
    }
}