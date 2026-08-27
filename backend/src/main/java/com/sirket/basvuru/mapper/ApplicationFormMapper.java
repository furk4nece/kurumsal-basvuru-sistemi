package com.sirket.basvuru.mapper;

import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.entity.ApplicationForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FormTypeMapper.class, UserMapper.class, AttachmentMapper.class})
public interface ApplicationFormMapper {
    ApplicationFormResponse toResponse(ApplicationForm applicationForm);
}