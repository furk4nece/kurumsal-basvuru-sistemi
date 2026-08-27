package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.request.FormTypeRequest;
import com.sirket.basvuru.dto.response.FormTypeResponse;

import java.util.List;

public interface FormTypeService {
    FormTypeResponse create(FormTypeRequest request);
    List<FormTypeResponse> getAll();
    FormTypeResponse getById(Long id);
    FormTypeResponse update(Long id, FormTypeRequest request);
    void delete(Long id);
}