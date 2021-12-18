package com.epam.brest.service;

import com.epam.brest.model.Category;
import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;

import java.util.List;

/**
 * Expense Service Interface.
 */
public interface ExpenseService {

    /**
     * Get all expenses
     * @return Expense list
     */
    List<Expense> findAllExpenses();

    /**
     * Get expense by id
     * @param expenseId - expense id
     * @return - Expense with given id
     */
    Expense getExpenseById(Integer expenseId);

    /**
     * Create new Expense
     * @param expense - expense to create
     * @return - id of the created expense
     * @throws - expense with a nonexistent category
     */
    Integer create(Expense expense) throws IncorrectExpense;

    /**
     * Update Expense
     * @param expense - expense to update
     * @return - number of updated rows
     */
    Integer update(Expense expense);

    /**
     * Delete Expense by id
     * @param expenseId - expense id to delete
     * @return - number of deleted rows
     */
    Integer delete(Integer expenseId);

    /**
     * Get id of last expense
     * @return - id of last expense
     */
    Integer getIdOfLastExpense();

    /**
     * Get count of all expenses
     * @return - count of all expenses
     */
    Integer count();
}
