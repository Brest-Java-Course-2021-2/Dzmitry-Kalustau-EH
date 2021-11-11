package com.epam.brest.service;

import com.epam.brest.model.dto.CategoryDto;

import java.util.List;

public interface CategoryDtoService {

    List<CategoryDto> findAllWithSumOfExpenses();

}
