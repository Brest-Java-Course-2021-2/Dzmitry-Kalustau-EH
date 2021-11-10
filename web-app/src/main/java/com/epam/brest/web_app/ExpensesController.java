package com.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ExpensesController {

    @GetMapping(value="/expenses")
    public final String expenses(Model model) {
        return "expenses";
    }

    @GetMapping(value="/edit-expenses/{id}")
    public final String gotoEditExpensesPage(@PathVariable Integer id, Model model) {
        return "edit-expenses";
    }

    @GetMapping(value="/edit-expenses/add")
    public final String gotoAddExpensesPage(Model model) {
        return "edit-expenses";
    }


}
