package com.epam.brest.service;

import com.epam.brest.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategories();

    Integer create(Category category);

    Integer count();

}
