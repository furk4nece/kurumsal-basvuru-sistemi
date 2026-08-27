package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.request.FormTypeRequest;
import com.sirket.basvuru.dto.response.FormTypeResponse;
import com.sirket.basvuru.entity.FormType;
import com.sirket.basvuru.mapper.FormTypeMapper;
import com.sirket.basvuru.repository.FormTypeRepository;
import com.sirket.basvuru.service.FormTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormTypeServiceImpl implements FormTypeService {

    private final FormTypeRepository formTypeRepository;
    private final FormTypeMapper formTypeMapper;

    @Override
    public FormTypeResponse create(FormTypeRequest request) {
        FormType formType = formTypeMapper.toEntity(request);
        FormType saved = formTypeRepository.save(formType);
        return formTypeMapper.toResponse(saved);
    }

    @Override
    public List<FormTypeResponse> getAll() {
        return formTypeRepository.findAll()
                .stream()
                .map(formTypeMapper::toResponse)
                .toList();
    }

    @Override
    public FormTypeResponse getById(Long id) {
        FormType formType = findEntityById(id);
        return formTypeMapper.toResponse(formType);
    }

    @Override
    public FormTypeResponse update(Long id, FormTypeRequest request) {
        FormType formType = findEntityById(id);
        formType.setName(request.getName());
        FormType updated = formTypeRepository.save(formType);
        return formTypeMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        FormType formType = findEntityById(id);
        formTypeRepository.delete(formType);
    }

    private FormType findEntityById(Long id) {
        return formTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form turu bulunamadi: id=" + id));
    }
}