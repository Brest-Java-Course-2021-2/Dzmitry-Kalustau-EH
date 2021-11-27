package com.epam.brest.web_app;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import com.epam.brest.web_app.validators.CategoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoriesController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriesController.class);

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
        model.addAttribute("isNew", false);
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "edit-categories";
    }

    @PostMapping(value = "/edit-categories/{id}")
    public String updateCategory(Category category) {
        logger.debug("update Category({}, {})", category);
        categoryService.update(category);
        return "redirect:/categories";
    }

    @GetMapping(value="/edit-categories")
    public final String gotoAddCategoriesPage(Model model) {
        logger.debug("gotoAddCategoriesPage({})", model);
        model.addAttribute("isNew", true);
        model.addAttribute("category", new Category());
        return "edit-categories";
    }

    @PostMapping(value = "/edit-categories")
    public final String addCategory(Category category, BindingResult result) {

        categoryValidator.validate(category, result);

        if (result.hasErrors()) {
            return "edit-categories";
        }

        logger.debug("add category ({}, {})", category);
        categoryService.create(category);
        return "redirect:/categories";
    }

    @GetMapping(value = "/edit-categories/{id}/delete")
    public final String deleteCategoryById(@PathVariable Integer id, Model model) {
        logger.debug("delete({},{})", id, model);
        categoryService.delete(id);
        return "redirect:/categories";
    }

}
