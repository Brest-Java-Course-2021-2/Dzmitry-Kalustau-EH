package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CategoryDto;
import com.epam.brest.service.CategoryDtoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:service-context-test.xml"})
@Transactional
class CategoryDtoServiceImplIT {

    @Autowired
    CategoryDtoService categoryDtoService;

    @Test
    public void shouldFindAllWithSumOfExpenses() {
        List<CategoryDto> categoriesDto = categoryDtoService.findAllWithSumOfExpenses();
        assertNotNull(categoriesDto);
        assertTrue(categoriesDto.size() > 0);

//        for (CategoryDto categoryDto : categoriesDto) {
//            System.out.println(categoryDto.getCategoryName() + " " + categoryDto.getSumOfExpense());
//        }

    }

}