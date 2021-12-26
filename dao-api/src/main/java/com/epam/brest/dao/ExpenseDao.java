package com.epam.brest.dao;

import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;

import java.util.List;

/**
 * Expense DAO Interface.
 */
public interface ExpenseDao {

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
    Integer create(Expense expense);

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
     * Get count of all expenses
     * @return - count of all expenses
     */
    Integer count();

    /**
     * Get id of last expense
     * @return - id of last expense
     */
    Integer getIdOfLastExpense();

}
