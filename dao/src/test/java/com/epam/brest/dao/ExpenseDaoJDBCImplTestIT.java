package com.epam.brest.dao;

import com.epam.brest.model.Category;
import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.testdb.SpringJdbcConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({ExpenseDaoJDBCImpl.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class ExpenseDaoJDBCImplTestIT {

    private final Logger logger = LogManager.getLogger(ExpenseDaoJDBCImplTestIT.class);

    private ExpenseDao expenseDao;

    @Autowired
    public ExpenseDaoJDBCImplTestIT(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    @Test
    void testFindAllExpenses() {
    logger.debug("Execute IT test findAllExpenses()");

    assertNotNull(expenseDao);
    assertNotNull(expenseDao.findAllExpenses());
    }

    @Test
    void testGetExpenseById() {
        logger.debug("Execute IT test getExpenseById()");

        assertNotNull(expenseDao);
        List<Expense> expenseList = expenseDao.findAllExpenses();
        if (expenseList.isEmpty()) {
            logger.info("expenseList was empty");
            expenseList.add(new Expense(1));
        }

        Expense expenseFromList = expenseList.get(0);
        Expense expenseFromDao = expenseDao.getExpenseById(expenseFromList.getExpenseId());
        assertEquals(expenseFromList.getExpenseId(), expenseFromDao.getExpenseId());
    }

    @Test
    void testGetIdOfLastExpense() {
        logger.debug("Execute IT test getIdOfLastExpense()");

        assertNotNull(expenseDao);

        List<Expense> expenseList = expenseDao.findAllExpenses();
        Expense expenseBeforeAdd = expenseList.get(expenseList.size() - 1);

        expenseDao.create(new Expense(9, LocalDate.now(), 1, BigDecimal.valueOf(1)));
        Integer idOfLastExpenseAfterAdd = expenseDao.getIdOfLastExpense();
        assertNotNull(idOfLastExpenseAfterAdd);
        assertEquals(expenseBeforeAdd.getExpenseId() +2, idOfLastExpenseAfterAdd);
    }

    @Test
    void testCreate() throws IncorrectExpense {
        logger.debug("Execute IT test create()");

        assertNotNull(expenseDao);
        int countOfExpensesBefore = (expenseDao.findAllExpenses()).size();
        Expense expense = new Expense(9, LocalDate.of(2021, 9,1), 1, new BigDecimal(15.5));
        Integer expenseId = expenseDao.create(expense);
        assertNotNull(expenseId);
        int countOfExpensesAfter = (expenseDao.findAllExpenses().size());
        assertEquals(countOfExpensesBefore, countOfExpensesAfter - 1);
    }

    @Test
    void testCreateIncorrectExpense() {

        logger.debug("Execute IT test createIncorrectExpense()");
        assertNotNull(expenseDao);
        Expense expense= new Expense(LocalDate.of(2021, 9,1), 50, new BigDecimal(15.5));
        assertThrows(IncorrectExpense.class, () -> {
            expenseDao.create(expense);
        });
    }

    @Test
    void testCount() {

        logger.debug("Execute IT test count()");
        assertNotNull(expenseDao);
        Integer countExpense = expenseDao.count();
        assertNotNull(countExpense);
        assertTrue(countExpense > 0);
        assertEquals(Integer.valueOf(8), countExpense);
    }

    @Test
    void testUpdate() throws IncorrectExpense {
        logger.debug("Execute IT test update()");

        assertNotNull(expenseDao);
        List<Expense> expenseList = expenseDao.findAllExpenses();
        if (expenseList.size()==0) {
            logger.info("expenseList was empty");
            Expense expense = new Expense(1, LocalDate.of(2021, 9,1), 1, new BigDecimal(15.5));
            expenseDao.create(expense);
        }
        Expense expenseBeforeUpdate = expenseList.get(0);
        Expense expense = new Expense(expenseBeforeUpdate.getExpenseId(), expenseBeforeUpdate.getDateOfExpense(), 2, new BigDecimal(5));
        assertNotEquals(expenseBeforeUpdate.getSumOfExpense(), expense.getSumOfExpense());
        expenseDao.update(expense);
        Expense expenseAfterUpdate = expenseDao.getExpenseById(expenseBeforeUpdate.getExpenseId());
        assertEquals(expenseAfterUpdate.getSumOfExpense(), expense.getSumOfExpense());
    }

    @Test
    void testDelete() throws IncorrectExpense {
        logger.debug("Execute IT test delete()");

        assertNotNull(expenseDao);
        Expense expenseForDelete = new Expense(9, LocalDate.of(2021, 9,1), 1, new BigDecimal(15.5));
        expenseDao.create(expenseForDelete);
        List<Expense> expenseListBeforeDelete = expenseDao.findAllExpenses();
        int sizeBeforeDelete = expenseListBeforeDelete.size();
        Expense expenseFromList = expenseListBeforeDelete.get(expenseListBeforeDelete.size()-1);
        expenseDao.delete(expenseFromList.getExpenseId());
        List<Expense> expenseListAfterDelete = expenseDao.findAllExpenses();
        int sizeAfterDelete = expenseListAfterDelete.size();
        assertEquals(sizeBeforeDelete, sizeAfterDelete + 1);
    }
}
