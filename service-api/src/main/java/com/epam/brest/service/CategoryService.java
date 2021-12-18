package com.epam.brest.service;

import com.epam.brest.model.Category;

import java.util.List;

/**
 * Category Service Interface.
 */
public interface CategoryService {

    /**
     * Get all categories
     * @return Category list
     */
    List<Category> findAllCategories();

    /**
     * Get category by id
     * @param categoryId - category id
     * @return - Category with given id
     */
    Category getCategoryById(Integer categoryId);

    /**
     * Create new Category
     * @param category - category to create
     * @return - id of the created category
     */
    Integer create(Category category);

    /**
     * Update Category
     * @param category - category to update
     * @return - number of updated rows
     */
    Integer update(Category category);

    /**
     * Delete Category by id
     * @param categoryId - category id to delete
     * @return - number of deleted rows
     */
    Integer delete(Integer categoryId);

    /**
     * Get count of all categories
     * @return - count of all categories
     */
    Integer count();

    /**
     * Get id of last category
     * @return - id of last category
     */
    Integer getIdOfLastCategory();
}
