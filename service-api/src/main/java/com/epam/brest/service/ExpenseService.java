package com.epam.brest.service;

import com.epam.brest.model.Category;
import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;

import java.util.List;

public interface ExpenseService {

    List<Expense> findAllExpenses();

    Expense getExpenseById(Integer expenseId);

    Integer create(Expense expense) throws IncorrectExpense;

    Integer update(Expense expense);

    Integer delete(Integer expenseId);
}
