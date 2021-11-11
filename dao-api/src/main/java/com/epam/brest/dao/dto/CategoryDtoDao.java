package com.epam.brest.dao.dto;

import com.epam.brest.model.dto.CategoryDto;

import java.util.List;

public interface CategoryDtoDao {

    List<CategoryDto> findAll();

}
