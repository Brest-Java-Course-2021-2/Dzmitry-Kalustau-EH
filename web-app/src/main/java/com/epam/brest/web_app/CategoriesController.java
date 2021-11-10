package com.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoriesController {

    @GetMapping(value="/categories")
    public final String categories(Model model) {
        return "categories";
    }

    @GetMapping(value="/edit-categories/{id}")
    public final String gotoEditCategoriesPage(@PathVariable Integer id, Model model) {
        return "edit-categories";
    }

    @GetMapping(value="/edit-categories/add")
    public final String gotoAddCategoriesPage(Model model) {
        return "edit-categories";
    }

}
