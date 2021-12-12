package com.epam.brest.dao;

import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;

import java.util.List;

public interface ExpenseDao {

    List<Expense> findAllExpenses();

    Expense getExpenseById(Integer expenseId);

    Integer create(Expense expense) throws IncorrectExpense;

    Integer update(Expense expense);

    Integer delete(Integer expenseId);

}
