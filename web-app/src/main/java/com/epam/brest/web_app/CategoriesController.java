package com.epam.brest.web_app;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import com.epam.brest.web_app.validators.CategoryValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoriesController {

    private static final Logger logger = LogManager.getLogger(CategoriesController.class);

    private final CategoryService categoryService;

    private final CategoryValidator categoryValidator;

    public CategoriesController(CategoryService categoryService, CategoryValidator categoryValidator) {
        this.categoryService = categoryService;
        this.categoryValidator = categoryValidator;
    }

    @GetMapping(value="/categories")
    public final String categories(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        return "categories";
    }

    @GetMapping(value="/edit-categories/{id}")
    public final String gotoEditCategoriesPage(@PathVariable Integer id, Model model) {
        logger.debug("gotoEditCategoriesPage({})", model);
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "edit-categories";
    }

    @PostMapping(value = "/edit-categories/{id}")
    public String updateCategory(Category category) {
        logger.debug("update Category({}, {})", category);
        categoryService.update(category);
        return "redirect:/categories";
    }

    @GetMapping(value="/add-categories")
    public final String gotoAddCategoriesPage(Model model) {
        logger.debug("gotoAddCategoriesPage({})", model);
        Integer lastCategoryId = categoryService.getIdOfLastCategory();
        Category category = new Category(lastCategoryId+1);
        model.addAttribute("category", category);
        return "add-categories";
    }

    @PostMapping(value = "/add-categories")
    public String addCategory(Category category) {
        logger.debug("add Category({}, {})", category);
        categoryService.create(category);
        return "redirect:/categories";
    }

    @GetMapping(value="/delete-categories/{id}")
    public final String gotoDeleteCategoriesPage(@PathVariable Integer id, Model model) {
        logger.debug("gotoDeleteCategoriesPage({})", model);
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "delete-categories";
    }

    @PostMapping(value = "/delete-categories/{id}")
    public String deleteCategory(Category category) {
        logger.debug("delete Category({}, {})", category);
        categoryService.delete(category.getCategoryId());
        return "redirect:/categories";
    }

}
