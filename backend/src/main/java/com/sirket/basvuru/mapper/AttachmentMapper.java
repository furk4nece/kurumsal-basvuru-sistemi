package com.sirket.basvuru.mapper;

import com.sirket.basvuru.dto.response.AttachmentResponse;
import com.sirket.basvuru.entity.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentResponse toResponse(Attachment attachment);
}