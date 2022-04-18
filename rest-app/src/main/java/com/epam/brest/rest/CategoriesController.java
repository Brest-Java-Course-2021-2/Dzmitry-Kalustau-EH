package com.epam.brest.rest;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@CrossOrigin
@Api(value = "categories", tags = "Categories API")
public class CategoriesController {

    private static final Logger logger = LogManager.getLogger(CategoriesController.class);

    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/categories")
    @ApiOperation(value = "Find all categories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Collection<Category> findAll() {

        logger.debug("findAll()");
        return categoryService.findAllCategories();
    }

    @GetMapping(value="/categories/{id}")
    @ApiOperation(value = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Category getCategoryById(@PathVariable @ApiParam(name = "Category id", example = "1") Integer id) {

        logger.debug("get category by id");
        return categoryService.getCategoryById(id);
    }

    @PostMapping(path = "categories", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Add category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "1", mediaType = "application/json"))),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public ResponseEntity<Integer> addCategory(@ApiParam("Category information for a new category to be created.") @RequestBody Category category) {

        logger.debug("add Category({})", category);
        Integer id = categoryService.create(category);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/categories", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Update category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "1", mediaType = "application/json"))),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public ResponseEntity<Integer> updateCategory(@ApiParam("Category information for a category to be updated.") @RequestBody Category category) {

        logger.debug("update Category({})", category);
        int result = categoryService.update(category);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @DeleteMapping(value = "/categories", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Delete category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "1", mediaType = "application/json"))),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public ResponseEntity<Integer> deleteCategory(@ApiParam("Id of the category to be deleted.") @RequestBody Integer id) {

        logger.debug("delete Category({})", id);
        int result = categoryService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/categories/last_id")
    @ApiOperation(value = "Get id of last category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "6", mediaType = "application/json"))),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Integer getIdOfLastCategory() {

        logger.debug("get id of last category");
        return categoryService.getIdOfLastCategory();
    }


}
