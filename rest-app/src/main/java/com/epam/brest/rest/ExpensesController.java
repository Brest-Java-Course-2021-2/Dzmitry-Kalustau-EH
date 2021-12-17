package com.epam.brest.rest;

import com.epam.brest.model.Category;
import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ExpenseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class ExpensesController {

    private static final Logger logger = LogManager.getLogger(CategoriesController.class);

    private final ExpenseService expenseService;

    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping(value = "/expenses")
    public final Collection<Expense> findAll() {

        logger.debug("findAll()");
        return expenseService.findAllExpenses();
    }

    @GetMapping(value="/expenses/{id}")
    public final Expense getExpenseById(@PathVariable Integer id) {

        logger.debug("get expense by id");
        return expenseService.getExpenseById(id);
    }

    @PostMapping(path = "expenses", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> addExpense(@RequestBody Expense expense) {

        logger.debug("add Expense({})", expense);
        Integer id = null;
        try {
            id = expenseService.create(expense);
        } catch (IncorrectExpense incorrectExpense) {
            incorrectExpense.printStackTrace();
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/expenses", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateExpense(@RequestBody Expense expense) {

        logger.debug("update Expense({})", expense);
        int result = expenseService.update(expense);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping(path = "expenses-delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> deleteExpense(@RequestBody Integer expenseId) {

        logger.debug("delete Expense({})", expenseId);
        Integer id = expenseService.delete(expenseId);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


//    @DeleteMapping(value = "/expenses", produces = {"application/json"})
//    public ResponseEntity<Integer> deleteExpense(@RequestBody Integer id) {
//
//        logger.debug("delete Expense({})", id);
//        int result = expenseService.delete(id);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @GetMapping(value="/expenses/last_id")
    public final Integer getExpenseById() {

        logger.debug("get id of last expense");
        return expenseService.getIdOfLastExpense();
    }

}