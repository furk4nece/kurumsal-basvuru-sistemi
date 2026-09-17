package com.sirket.basvuru.mapper;

import com.sirket.basvuru.dto.request.FormTypeRequest;
import com.sirket.basvuru.dto.response.FormTypeResponse;
import com.sirket.basvuru.entity.FormType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FormTypeMapper {

    @Mapping(target = "id", ignore = true)
    FormType toEntity(FormTypeRequest request);

    FormTypeResponse toResponse(FormType formType);
}