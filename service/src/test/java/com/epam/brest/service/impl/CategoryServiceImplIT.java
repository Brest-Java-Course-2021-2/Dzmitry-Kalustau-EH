package com.epam.brest.service.impl;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:service-context-test.xml"})
@Transactional
class CategoryServiceImplIT {

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldReturnCount() {
        assertNotNull(categoryService);
        Integer countCategory = categoryService.count();
        assertNotNull(countCategory);
        assertTrue(countCategory > 0);
        assertEquals(Integer.valueOf(6), countCategory);
    }

    @Test
    void create() {
        assertNotNull(categoryService);
        Integer categoryCountBefore = categoryService.count();
        Category category = new Category("Tickets");
        Integer categoryId = categoryService.create(category);
        assertNotNull(categoryId);
        assertEquals(categoryCountBefore, categoryService.count() - 1);
    }

    @Test
    void tryToCreateEqualsCategory() {
        assertNotNull(categoryService);
        Category category = new Category("Restaurant");
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.create(category);
            categoryService.create(category);
        });
    }

}