package com.epam.brest.service.impl;

import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.service.ExpenseService;
import com.epam.brest.service.config.ServiceTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({ServiceTestConfig.class})
@Transactional
public class ExpenseServiceImplIT {

    private final Logger logger = LogManager.getLogger(ExpenseServiceImplIT.class);

    @Autowired
    private ExpenseService expenseService;

    @Test
    void testFindAllExpenses() {

        logger.debug("Execute IT test findAllExpenses()");
        assertNotNull(expenseService);
        assertNotNull(expenseService.findAllExpenses());
    }

    @Test
    void testGetExpenseById() {

        logger.debug("Execute IT test getExpenseById()");
        assertNotNull(expenseService);
        List<Expense> expenseList = expenseService.findAllExpenses();
        if (expenseList.isEmpty()) {
            logger.info("expenseList was empty");
            expenseList.add(new Expense(1));
        }

        Expense expenseFromList = expenseList.get(0);
        Expense expenseFromDao = expenseService.getExpenseById(expenseFromList.getExpenseId());
        assertEquals(expenseFromList.getExpenseId(), expenseFromDao.getExpenseId());
    }

    @Test
    void testGetIdOfLastExpense() {
        logger.debug("Execute IT test getIdOfLastExpense()");

        assertNotNull(expenseService);

        List<Expense> expenseList = expenseService.findAllExpenses();
        Expense expenseBeforeAdd = expenseList.get(expenseList.size() - 1);

        expenseService.create(new Expense(9, LocalDate.now(), 1, BigDecimal.valueOf(1)));
        Integer idOfLastExpenseAfterAdd = expenseService.getIdOfLastExpense();
        assertNotNull(idOfLastExpenseAfterAdd);
        assertEquals(expenseBeforeAdd.getExpenseId() +2, idOfLastExpenseAfterAdd);
    }

    @Test
    void testCreate() throws IncorrectExpense {
        logger.debug("Execute IT test create()");

        assertNotNull(expenseService);
        int countOfExpensesBefore = (expenseService.findAllExpenses()).size();
        Expense expense = new Expense(9, LocalDate.of(2021, 9,1), 1, new BigDecimal(15.5));
        Integer expenseId = expenseService.create(expense);
        assertNotNull(expenseId);
        int countOfExpensesAfter = (expenseService.findAllExpenses().size());
        assertEquals(countOfExpensesBefore, countOfExpensesAfter - 1);
    }

    @Test
    void testCreateIncorrectExpense() {

        logger.debug("Execute IT test createIncorrectExpense()");
        assertNotNull(expenseService);
        Expense expense= new Expense(LocalDate.of(2021, 9,1), 50, new BigDecimal(15.5));
        assertThrows(IncorrectExpense.class, () -> {
            expenseService.create(expense);
        });
    }

    @Test
    void testCount() {

        logger.debug("Execute IT test count()");
        assertNotNull(expenseService);
        Integer countExpense = expenseService.count();
        assertNotNull(countExpense);
        assertTrue(countExpense > 0);
        assertEquals(Integer.valueOf(8), countExpense);
    }

    @Test
    void testUpdate() throws IncorrectExpense {
        logger.debug("Execute IT test update()");

        assertNotNull(expenseService);
        List<Expense> expenseList = expenseService.findAllExpenses();
        if (expenseList.size()==0) {
            logger.info("expenseList was empty");
            Expense expense = new Expense(1, LocalDate.of(2021, 9,1), 1, new BigDecimal(15.5));
            expenseService.create(expense);
        }
        Expense expenseBeforeUpdate = expenseList.get(0);
        Expense expense = new Expense(expenseBeforeUpdate.getExpenseId(), expenseBeforeUpdate.getDateOfExpense(), 2, new BigDecimal(5));
        assertNotEquals(expenseBeforeUpdate.getSumOfExpense(), expense.getSumOfExpense());
        expenseService.update(expense);
        Expense expenseAfterUpdate = expenseService.getExpenseById(expenseBeforeUpdate.getExpenseId());
        assertEquals(expenseAfterUpdate.getSumOfExpense(), expense.getSumOfExpense());
    }

    @Test
    void testDelete() throws IncorrectExpense {
        logger.debug("Execute IT test delete()");

        assertNotNull(expenseService);
        Expense expenseForDelete = new Expense(9, LocalDate.of(2021, 9,1), 1, new BigDecimal(15.5));
        expenseService.create(expenseForDelete);
        List<Expense> expenseListBeforeDelete = expenseService.findAllExpenses();
        int sizeBeforeDelete = expenseListBeforeDelete.size();
        Expense expenseFromList = expenseListBeforeDelete.get(expenseListBeforeDelete.size()-1);
        expenseService.delete(expenseFromList.getExpenseId());
        List<Expense> expenseListAfterDelete = expenseService.findAllExpenses();
        int sizeAfterDelete = expenseListAfterDelete.size();
        assertEquals(sizeBeforeDelete, sizeAfterDelete + 1);
    }
}
