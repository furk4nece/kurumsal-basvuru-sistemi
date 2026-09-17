package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import com.sirket.basvuru.entity.Attachment;
import com.sirket.basvuru.service.AttachmentService;
import com.sirket.basvuru.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Tag(name = "Dosyalar", description = "Basvuruya ek dosya yukleme, indirme ve silme")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload/{applicationFormId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Basvuruya dosya yukler")
    public ResponseEntity<AttachmentResponse> upload(@PathVariable Long applicationFormId,
                                                      @RequestParam("file") MultipartFile file,
                                                      Authentication authentication) {
        return ResponseEntity.ok(attachmentService.upload(applicationFormId, file, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ek dosyayi siler")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        attachmentService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Ek dosyayi indirir", description = "Sadece basvuru sahibi veya ADMIN indirebilir")
    public ResponseEntity<Resource> download(@PathVariable Long id, Authentication authentication) {
        Attachment attachment = attachmentService.getForDownload(id, authentication.getName());
        Resource resource = fileStorageService.load(attachment.getFileName());
        String encodedName = URLEncoder.encode(attachment.getOriginalName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(resource);
    }
}