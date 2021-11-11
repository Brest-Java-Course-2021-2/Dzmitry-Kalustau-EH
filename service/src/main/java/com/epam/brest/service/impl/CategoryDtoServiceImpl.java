package com.epam.brest.service.impl;

import com.epam.brest.dao.CategoryDao;
import com.epam.brest.dao.dto.CategoryDtoDao;
import com.epam.brest.model.dto.CategoryDto;
import com.epam.brest.service.CategoryDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CategoryDtoServiceImpl implements CategoryDtoService {

    CategoryDtoDao categoryDtoDao;

    public CategoryDtoServiceImpl(CategoryDtoDao categoryDtoDao) {
        this.categoryDtoDao = categoryDtoDao;
    }

    @Override
    public List<CategoryDto> findAllWithSumOfExpenses() {
        return categoryDtoDao.findAllWithSumOfExpenses();
    }
}
