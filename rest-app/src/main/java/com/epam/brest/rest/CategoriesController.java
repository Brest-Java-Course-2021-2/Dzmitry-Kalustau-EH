package com.epam.brest.rest;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@CrossOrigin
public class CategoriesController {

    private static final Logger logger = LogManager.getLogger(CategoriesController.class);

    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/categories")
    public final Collection<Category> findAll() {

        logger.debug("findAll()");
        return categoryService.findAllCategories();
    }

    @GetMapping(value="/categories/{id}")
    public final Category getCategoryById(@PathVariable Integer id) {

        logger.debug("get category by id");
        return categoryService.getCategoryById(id);
    }

    @PostMapping(path = "categories", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> addCategory(@RequestBody Category category) {

        logger.debug("add Category({})", category);
        Integer id = categoryService.create(category);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/categories", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateCategory(@RequestBody Category category) {

        logger.debug("update Category({})", category);
        int result = categoryService.update(category);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @DeleteMapping(value = "/categories", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> deleteCategory(@RequestBody Integer id) {

        logger.debug("delete Category({})", id);
        int result = categoryService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/categories/last_id")
    public final Integer getCategoryById() {

        logger.debug("get id of last category");
        return categoryService.getIdOfLastCategory();
    }


}
