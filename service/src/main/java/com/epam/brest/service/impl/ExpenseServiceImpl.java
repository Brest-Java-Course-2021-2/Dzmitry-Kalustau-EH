package com.epam.brest.service.impl;

import com.epam.brest.dao.ExpenseDao;
import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.service.ExpenseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final Logger logger = LogManager.getLogger(ExpenseServiceImpl.class);

    private ExpenseDao expenseDao;

    public ExpenseServiceImpl(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    @Override
    public List<Expense> findAllExpenses() {

        logger.debug("find all expenses");
        return expenseDao.findAllExpenses();
    }

    @Override
    public Expense getExpenseById(Integer expenseId) {

        logger.debug("get expense by id {}", expenseId);
        return expenseDao.getExpenseById(expenseId);
    }

    @Override
    public Integer create(Expense expense) throws IncorrectExpense {

        logger.debug("create expense {}", expense);
        return expenseDao.create(expense);
    }

    @Override
    public Integer update(Expense expense) {

        logger.debug("update expense {}", expense);
        return expenseDao.update(expense);
    }

    @Override
    public Integer delete(Integer expenseId) {

        logger.debug("delete expense by id {}", expenseId);
        return expenseDao.delete(expenseId);
    }
}
