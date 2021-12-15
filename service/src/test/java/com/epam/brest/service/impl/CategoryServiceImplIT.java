package com.epam.brest.service.impl;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.config.ServiceTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({ServiceTestConfig.class})
@Transactional
class CategoryServiceImplIT {

    private final Logger logger = LogManager.getLogger(CategoryServiceImplIT.class);

    @Autowired
    private CategoryService categoryService;

    @Test
    void testFindAll() {

        logger.debug("Execute IT test findAll()");
        assertNotNull(categoryService);
        assertNotNull(categoryService.findAllCategories());
    }

    @Test
    void testCount() {

        logger.debug("Execute IT test count()");
        assertNotNull(categoryService);
        Integer countCategory = categoryService.count();
        assertNotNull(countCategory);
        assertTrue(countCategory > 0);
        assertEquals(Integer.valueOf(6), countCategory);
    }

    @Test
    void testCreate() {

        logger.debug("Execute IT test create()");
        assertNotNull(categoryService);
        Integer categoryCountBefore = categoryService.count();
        Category category = new Category("Tickets");
        Integer categoryId = categoryService.create(category);
        assertNotNull(categoryId);
        assertEquals(categoryCountBefore, categoryService.count() - 1);
    }

    @Test
    void testCreateEqualsCategory() {

        logger.debug("Execute IT test CreateEqualsCategory()");
        assertNotNull(categoryService);
        Category category = new Category("Restaurant");
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.create(category);
            categoryService.create(category);
        });
    }

    @Test
    void testGetCategoryByID() {
        logger.debug("Execute IT test getCategoryById()");
        List<Category> categoryList = categoryService.findAllCategories();
        if (categoryList.size() == 0) {
            categoryList.add(new Category("Test Category"));
        }
        Category categoryFromList = categoryList.get(0);
        Category categoryFromDao = categoryService.getCategoryById(categoryFromList.getCategoryId());
        assertEquals(categoryFromList.getCategoryName(), categoryFromDao.getCategoryName());
    }

    @Test
    void testUpdate() {

        logger.debug("Execute IT test update()");
        List<Category> categoryList = categoryService.findAllCategories();
        if (categoryList.size() == 0) {
            categoryService.create(new Category("Test Category"));
            categoryList = categoryService.findAllCategories();
        }

        Category categoryFromList = categoryList.get(0);
        categoryFromList.setCategoryName(categoryFromList.getCategoryName() + "_test");
        categoryService.update(categoryFromList);

        Category categoryFromDao = categoryService.getCategoryById(categoryFromList.getCategoryId());
        assertEquals(categoryFromList.getCategoryName(), categoryFromDao.getCategoryName());
    }

    @Test
    void testDelete() {

        logger.debug("Execute IT test delete()");
        categoryService.create(new Category("Test Category"));
        List<Category> categoryList = categoryService.findAllCategories();

        categoryService.delete(categoryList.get(categoryList.size()-1).getCategoryId());
        assertEquals(categoryList.size()-1, categoryService.findAllCategories().size());
    }

}