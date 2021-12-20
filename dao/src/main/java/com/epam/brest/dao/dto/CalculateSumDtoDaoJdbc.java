package com.epam.brest.dao.dto;

import com.epam.brest.dao.ExpenseDaoJDBCImpl;
import com.epam.brest.model.Expense;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class CalculateSumDtoDaoJdbc implements CalculateSumDtoDao {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoDaoJdbc.class);

    private final String SQL_FIND_ALL_SUM_OF_EXPENSES = "SELECT\n" +
            "\tc.category_name AS categoryName,\n" +
            "\tsum(e.price) AS sumOfExpense\n" +
            "FROM\n" +
            "\texpense e\n" +
            "INNER JOIN category c ON\n" +
            "\te.category_id = c.category_id\n" +
            "GROUP BY\n" +
            "\te.category_id\n" +
            "ORDER BY\n" +
            "sumOfExpense";


    private final String SQL_FIND_SUM_OF_EXPENSES_BETWEEN_DATES = "SELECT\n" +
            "\tc.category_name AS categoryName,\n" +
            "\tsum(e.price) AS sumOfExpense\n" +
            "FROM\n" +
            "\texpense e\n" +
            "INNER JOIN category c ON\n" +
            "\te.category_id = c.category_id\n" +
            "WHERE date BETWEEN\n" +
            "\t:dateFrom AND :dateTo\n" +
            "GROUP BY\n" +
            "\te.category_id\n" +
            "ORDER BY\n" +
            "sumOfExpense";

    private final String SQL_ALL_EXPENSES = "SELECT * FROM expense";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private LocalDateContainer localDateContainer;

    public CalculateSumDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.localDateContainer = new LocalDateContainer(getAllExpenseDates().first(), getAllExpenseDates().last());
    }

    @Override
    public List<CalculateSumDto> findAllWithSumOfExpenses()  {

        logger.debug("find All With Sum Of Expenses");
        LocalDate localDateFrom = localDateContainer.getDateFrom();
        LocalDate localDateTo = localDateContainer.getDateTo();

        Map<String, LocalDate> paramsOfSQL = new HashMap<>();

        paramsOfSQL.put("dateFrom", localDateFrom);
        paramsOfSQL.put("dateTo",  localDateTo);

        List<CalculateSumDto> calculateSumDtoList = namedParameterJdbcTemplate.query(SQL_FIND_SUM_OF_EXPENSES_BETWEEN_DATES,
        paramsOfSQL, BeanPropertyRowMapper.newInstance(CalculateSumDto.class));
        addTotalSum(calculateSumDtoList);
        return calculateSumDtoList;
    }

    //Add final Sum of all expenses (Total Sum in the table)
    private List<CalculateSumDto> addTotalSum(List<CalculateSumDto> calculateSumDtoList) {

        logger.debug("Add total Sum to calculateSumDto List");
        BigDecimal finalSumofExpenses = new BigDecimal(0);
        for (CalculateSumDto sum : calculateSumDtoList) {
            finalSumofExpenses = finalSumofExpenses.add(sum.getSumOfExpense());
        }
        calculateSumDtoList.add(new CalculateSumDto("Total Sum", finalSumofExpenses));
        return calculateSumDtoList;
    }

    @Override
    public LocalDateContainer getLocalDateContainer() {
        return localDateContainer;
    }

    @Override
    public void editLocalDateContainer(LocalDate localDateFrom, LocalDate localDateTo) {

        logger.debug("edit localDateConteiner with dates {}, {}", localDateFrom, localDateTo);
        localDateContainer.setDateFrom(localDateFrom);
        localDateContainer.setDateTo(localDateTo);
    }

    //find all dates from expeses
    private TreeSet<LocalDate> getAllExpenseDates() {

        logger.debug("get all expense dates");

        List<Expense> expenseList = namedParameterJdbcTemplate.query(SQL_ALL_EXPENSES, (resultSet, rowNum) -> {
            Expense expense = new Expense();
            expense.setExpenseId(resultSet.getInt("expense_id"));
            expense.setDateOfExpense((resultSet.getDate("date")).toLocalDate());
            expense.setCategoryId(resultSet.getInt("category_id"));
            expense.setSumOfExpense(resultSet.getBigDecimal("price"));
            return expense;
        });

        TreeSet<LocalDate> localDateSet = new TreeSet<LocalDate>();
            for (Expense expense : expenseList) {
                localDateSet.add(expense.getDateOfExpense());
        }

        return localDateSet;
    }
}
