package com.sirket.basvuru.dto.response;

import com.sirket.basvuru.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationFormResponse {
    private Long id;
    private String title;
    private String description;
    private FormTypeResponse formType;
    private ApplicationStatus status;
    private UserResponse applicant;
    private List<AttachmentResponse> attachments;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}