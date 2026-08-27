package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import com.sirket.basvuru.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(value = "/upload/{applicationFormId}", consumes = "multipart/form-data")
    public ResponseEntity<AttachmentResponse> upload(@PathVariable Long applicationFormId,
                                                       @RequestParam("file") MultipartFile file,
                                                       Authentication authentication) {
        return ResponseEntity.ok(attachmentService.upload(applicationFormId, file, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        attachmentService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}