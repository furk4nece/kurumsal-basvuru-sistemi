package com.sirket.basvuru.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private String originalName;
    private LocalDateTime uploadDate;
}