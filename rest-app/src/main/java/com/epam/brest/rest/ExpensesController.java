package com.epam.brest.rest;

import com.epam.brest.model.Expense;
import com.epam.brest.service.ExpenseService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
@Api(value = "expenses", tags = "Expenses API")
public class ExpensesController {

    private static final Logger logger = LogManager.getLogger(CategoriesController.class);

    private final ExpenseService expenseService;

    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping(value = "/expenses")
    @ApiOperation(value = "Find all expenses", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Collection<Expense> findAll() {

        logger.debug("findAll()");
        return expenseService.findAllExpenses();
    }

    @GetMapping(value="/expenses/{id}")
    @ApiOperation(value = "Get expense by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Expense getExpenseById(@PathVariable @ApiParam(name = "Expense id", example = "1") Integer id) {

        logger.debug("get expense by id");
        return expenseService.getExpenseById(id);
    }

    @PostMapping(path = "expenses", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Add expense")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "1", mediaType = "application/json"))),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public ResponseEntity<Integer> addExpense(@ApiParam("Expense information for a new expense to be created.") @RequestBody Expense expense) {

        logger.debug("add Expense({})", expense);
        Integer id = null;
            id = expenseService.create(expense);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/expenses", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Update expense")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "1", mediaType = "application/json"))),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public ResponseEntity<Integer> updateExpense(@ApiParam("Expense information for an expense to be updated.") @RequestBody Expense expense) {

        logger.debug("update Expense({})", expense);
        int result = expenseService.update(expense);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @DeleteMapping(value = "/expenses", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Delete expense by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "1", mediaType = "application/json"))),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public ResponseEntity<Integer> deleteExpense(@ApiParam(value = "Id of the expense to be deleted.", example = "6") @RequestBody Integer id) {

        logger.debug("delete Expense({})", id);
        int result = expenseService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/expenses/last_id")
    @ApiOperation(value = "Get id of last expense")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(@ExampleProperty(value = "6", mediaType = "application/json"))),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Integer getIdOfLastExpense() {

        logger.debug("get id of last expense");
        return expenseService.getIdOfLastExpense();
    }

}
