package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentResponse upload(Long applicationFormId, MultipartFile file, String requesterEmail);
    void delete(Long attachmentId, String requesterEmail);
}