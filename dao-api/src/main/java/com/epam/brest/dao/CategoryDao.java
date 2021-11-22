package com.epam.brest.dao;

import com.epam.brest.model.Category;

import java.util.List;

public interface CategoryDao {

    List<Category> findAllCategories();

    Category getCategoryById(Integer categoryId);

    Integer create(Category category);

    Integer update(Category category);

    Integer delete(Integer categoryId);

    Integer count();
}
