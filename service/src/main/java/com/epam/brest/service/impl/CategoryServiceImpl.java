package com.epam.brest.service.impl;

import com.epam.brest.dao.CategoryDao;
import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    private CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryDao.findAllCategories();
    }

    @Override
    public Integer create(Category category) {
        logger.debug("create({})", category);
        return categoryDao.create(category);
    }

    @Override
    public Integer count() {
        logger.debug("count()");
        return categoryDao.count();
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        logger.debug("get Category By Id {}", categoryId);
        return categoryDao.getCategoryById(categoryId);
    }

    @Override
    public Integer update(Category category) {
        logger.debug("update category {}", category);
        return categoryDao.update(category);
    }

    @Override
    public Integer delete(Integer categoryId) {
        logger.debug("delete category by Id {}", categoryId);
        return categoryDao.delete(categoryId);
    }
}
