package com.epam.brest.web_app;

import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ExpenseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
public class ExpensesController {

    private static final Logger logger = LogManager.getLogger(ExpensesController.class);

    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public ExpensesController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping(value="/expenses")
    public final String expenses(Model model) {

        logger.debug("get all expenses({})", model);
        List<Expense> expenseListOriginal = expenseService.findAllExpenses();
        List<?> expenseList = setCategoryNameById(expenseListOriginal);
        model.addAttribute("expenses", expenseList);
        return "expenses";
    }

    @GetMapping(value="/edit-expenses/{id}")
    public final String gotoEditExpensesPage(@PathVariable Integer id, Model model) {

        logger.debug("gotoEditExpensesPage({})", model);

        model.addAttribute("expense", expenseService.getExpenseById(id));
        return "edit-expenses";
    }

    @PostMapping(value = "/edit-expenses/{id}")
    public String updateExpense(Expense expense) {
        logger.debug("update Expense({}, {})", expense);
        expenseService.update(expense);
        return "redirect:/expenses";
    }


    @GetMapping(value="/add-expenses")
    public final String gotoAddExpensesPage(Model model) {
        logger.debug("gotoAddExpensesPage({})", model);

        Integer lastExpenseId = expenseService.getIdOfLastExpense();
        LocalDate currentDate = LocalDate.now();

        Expense expense = new Expense(lastExpenseId + 1, currentDate);
        model.addAttribute("expense", expense);
        return "add-expenses";
    }

    @PostMapping(value = "/add-expenses")
    public String addExpense(Expense expense) {
        logger.debug("add Expense({})", expense);
        try {
            expenseService.create(expense);
        } catch (IncorrectExpense e) {
            e.printStackTrace();
        }
        return "redirect:/expenses";
    }


    @GetMapping(value="/delete-expenses/{id}")
    public final String gotoDeleteExpensesPage(@PathVariable Integer id, Model model) {
        logger.debug("gotoDeleteExpensesPage({})", model);

        model.addAttribute("expense", expenseService.getExpenseById(id));
        return "delete-expenses";
    }

    @PostMapping(value = "/delete-expenses/{id}")
    public String deleteExpense(Expense expense) {
        logger.debug("delete Expense({}, {})", expense);
        expenseService.delete(expense.getExpenseId());
        return "redirect:/expenses";
    }



    private List<?> setCategoryNameById(List<?> expenseList) {
        List<LinkedHashMap<String, Object>> expenseListWithMaps = (List<LinkedHashMap<String, Object>>) expenseList;
        for (LinkedHashMap<String, Object> linkedHashMapElement : expenseListWithMaps) {
            Integer categoryId = (Integer) linkedHashMapElement.get("categoryId");
            String categoryName = categoryService.getCategoryById(categoryId).getCategoryName();
            linkedHashMapElement.put("categoryName", categoryName);
        }
        return expenseListWithMaps;
    }
}
