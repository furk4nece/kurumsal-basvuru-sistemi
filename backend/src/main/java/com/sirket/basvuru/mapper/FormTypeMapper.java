package com.sirket.basvuru.mapper;

import com.sirket.basvuru.dto.request.FormTypeRequest;
import com.sirket.basvuru.dto.response.FormTypeResponse;
import com.sirket.basvuru.entity.FormType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FormTypeMapper {

    FormType toEntity(FormTypeRequest request);

    FormTypeResponse toResponse(FormType formType);
}