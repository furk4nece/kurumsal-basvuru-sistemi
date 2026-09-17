package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import com.sirket.basvuru.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentResponse upload(Long applicationFormId, MultipartFile file, String requesterEmail);
    void delete(Long attachmentId, String requesterEmail);
    Attachment getForDownload(Long attachmentId, String requesterEmail);
}