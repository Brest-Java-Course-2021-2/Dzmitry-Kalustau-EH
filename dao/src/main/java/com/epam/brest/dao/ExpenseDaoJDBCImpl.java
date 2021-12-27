package com.epam.brest.dao;

import com.epam.brest.model.Category;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.model.Expense;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExpenseDaoJDBCImpl implements ExpenseDao {

    private final Logger logger = LogManager.getLogger(ExpenseDaoJDBCImpl.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_ALL_EXPENSES = "SELECT * FROM expense";
    private final String SQL_EXPENSE_BY_ID = "SELECT * FROM expense WHERE expense_id = :expenseId";
    private final String SQL_CREATE_EXPENSE = "INSERT INTO expense(expense_id, date, category_id, price) VALUES (:expense_id, :date, :category_id, :price)";
    private final String SQL_UPDATE_EXPENSE = "UPDATE expense SET category_id = :categoryId, price = :price WHERE expense_id = :expenseId";
    private final String SQL_DELETE_EXPENSE_BY_ID = "DELETE FROM expense WHERE expense_id = :expenseId";
    private final String SQL_ALL_CATEGORIES = "SELECT c.category_id, c.category_name FROM category c ORDER BY c.category_id";
    private final String SQL_SELECT_COUNT = "SELECT count(*) FROM expense";


    public ExpenseDaoJDBCImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Expense> findAllExpenses() {

        logger.debug("Find all expenses");
        return namedParameterJdbcTemplate.query(SQL_ALL_EXPENSES, new ExpenseRowMapper());
    }

    @Override
    public Expense getExpenseById(Integer expenseId) {

        logger.debug("Get expense by id = {}", expenseId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("expenseId", expenseId);
        return namedParameterJdbcTemplate.queryForObject(SQL_EXPENSE_BY_ID, sqlParameterSource, new ExpenseRowMapper());
    }

    @Override
    public Integer create(Expense expense) {

        logger.debug("Create expense {}", expense);

        if (expense.getDateOfExpense() == null) {
            logger.info("LocalDate was null in {}", expense);
            expense.setDateOfExpense(LocalDate.now());
        }

        if (!isExist(expense.getCategoryId())) {
            logger.error("such categoryId = {} with expenseId = {} does not exists ", expense.getCategoryId(), expense.getExpenseId());
            throw new IncorrectExpense("Such value of category does not exists", expense.getCategoryId());
        }
        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("expense_id", expense.getExpenseId());
        mapParams.put("date", expense.getDateOfExpense());
        mapParams.put("category_id", expense.getCategoryId());
        mapParams.put("price", expense.getSumOfExpense());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(mapParams);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_EXPENSE, sqlParameterSource, keyHolder);
        return (Integer) keyHolder.getKey();
    }

    @Override
    public Integer update(Expense expense) {
        logger.debug("Update expense {}", expense);

        if (expense.getDateOfExpense() == null) {
            logger.info("LocalDate was null in {}", expense);
            expense.setDateOfExpense(LocalDate.now());
        }

        Map<String, Object> paramsOfSql = new HashMap<>();
        paramsOfSql.put("categoryId", expense.getCategoryId());
        paramsOfSql.put("price", expense.getSumOfExpense());
        paramsOfSql.put("expenseId", expense.getExpenseId());

        return namedParameterJdbcTemplate.update(SQL_UPDATE_EXPENSE, paramsOfSql);
    }

    @Override
    public Integer delete(Integer expenseId) {
        logger.debug("Delete expense by id = {}", expenseId);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("expenseId", expenseId);
        return namedParameterJdbcTemplate.update(SQL_DELETE_EXPENSE_BY_ID, sqlParameterSource);
    }

    private boolean isExist(Integer categoryId) {

        logger.debug("Check for the passed categoryId = {}", categoryId);
        boolean isExist = false;
        List<Category> categoryList = namedParameterJdbcTemplate.query(SQL_ALL_CATEGORIES, new CategoryRowMapper());
        for (Category category : categoryList) {
            if (category.getCategoryId() == categoryId) {
                isExist = true;
                return isExist;
            }
        }
        return isExist;
    }


    @Override
    public Integer count() {
        logger.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(SQL_SELECT_COUNT, new MapSqlParameterSource(), Integer.class);
    }

    @Override
    public Integer getIdOfLastExpense() {
        logger.debug("getIdOfLastExpense");
        List<Expense> expenseList = namedParameterJdbcTemplate.query(SQL_ALL_EXPENSES, new ExpenseRowMapper());
        Integer idOfLastExpense = 1;
        if (expenseList.isEmpty()) {
            return idOfLastExpense;
        }

        idOfLastExpense = findMissedId(expenseList);
        return idOfLastExpense;
    }


    private Integer findMissedId(List<Expense> expenseList) {
        Integer missedId = expenseList.size()+1;
        List<Integer> expenseIdList = expenseList.stream().map((expense) -> expense.getExpenseId()).collect(Collectors.toList());
        Integer[] numbersArray = new Integer[expenseIdList.size()];
        for (int i=0; i<numbersArray.length; i++) numbersArray[i] = i + 1;

        if (expenseIdList.get(0) != numbersArray[0]) {
            return 1;
        }

        for (int i=1; i<expenseIdList.size(); i++) {
            if (expenseIdList.get(i) != numbersArray[i]) {
                return expenseIdList.get(i-1) + 1;
            }
        }
        return missedId;
    }


    private class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet resultSet, int i) throws SQLException {
            Category category = new Category();
            category.setCategoryId(resultSet.getInt("category_id"));
            category.setCategoryName(resultSet.getString("category_name"));
            return category;
        }
    }

    private class ExpenseRowMapper implements RowMapper<Expense> {
        @Override
        public Expense mapRow(ResultSet resultSet, int i) throws SQLException {
            Expense expense = new Expense();
            expense.setExpenseId(resultSet.getInt("expense_id"));
            expense.setDateOfExpense((resultSet.getDate("date")).toLocalDate());
            expense.setCategoryId(resultSet.getInt("category_id"));
            expense.setSumOfExpense(resultSet.getBigDecimal("price"));
            return expense;
        }
    }
}
