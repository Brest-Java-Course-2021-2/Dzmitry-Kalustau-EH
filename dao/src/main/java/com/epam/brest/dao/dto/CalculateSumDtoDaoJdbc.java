package com.epam.brest.dao.dto;

import com.epam.brest.model.Expense;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class CalculateSumDtoDaoJdbc implements CalculateSumDtoDao, InitializingBean {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoDaoJdbc.class);

    @Value("${SQL_FIND_ALL_SUM_OF_EXPENSES}")
    public String sqlFindAllSumOfExpenses;

    @Value("${SQL_FIND_SUM_OF_EXPENSES_BETWEEN_DATES}")
    public String sqlFindSumOfExpensesBetweenDates;

    @Value("${SQL_ALL_EXPENSES}")
    public String SQL_ALL_EXPENSES;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private LocalDateContainer localDateContainer;
    private CalculateSumDto calculateSumDtoTotalSum;

    public CalculateSumDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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

        List<CalculateSumDto> calculateSumDtoList = namedParameterJdbcTemplate.query(sqlFindSumOfExpensesBetweenDates,
        paramsOfSQL, BeanPropertyRowMapper.newInstance(CalculateSumDto.class));
        addTotalSum(calculateSumDtoList);
        return calculateSumDtoList;
    }

    @Override
    public List<CalculateSumDto> findSumOfExpensesByDates(LocalDate dateFrom, LocalDate dateTo) {

        logger.debug("find dates between {} and {}", dateFrom, dateTo);
        Map<String, LocalDate> paramsOfSQL = new HashMap<>();

        paramsOfSQL.put("dateFrom", dateFrom);
        paramsOfSQL.put("dateTo",  dateTo);

        List<CalculateSumDto> calculateSumDtoList = namedParameterJdbcTemplate.query(sqlFindSumOfExpensesBetweenDates,
        paramsOfSQL, BeanPropertyRowMapper.newInstance(CalculateSumDto.class));
        return calculateSumDtoList;
    }

    //Add final Sum of all expenses (Total Sum in the table)
    private void addTotalSum(List<CalculateSumDto> calculateSumDtoList) {

        logger.debug("Add total Sum to CalculateSumDto Total Sum");
        BigDecimal finalSumOfExpenses = new BigDecimal(0);
        for (CalculateSumDto sum : calculateSumDtoList) {
            finalSumOfExpenses = finalSumOfExpenses.add(sum.getSumOfExpense());
        }
        calculateSumDtoTotalSum = new CalculateSumDto("Total Sum", finalSumOfExpenses);
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

    @Override
    public CalculateSumDto getTotalSum() {

        logger.debug("getTotalSum()");
        if (calculateSumDtoTotalSum == null) {
            logger.info("CalculateSumDtoTotalSum was null");
            return new CalculateSumDto("Total Sum", new BigDecimal("0"));
        }
        return calculateSumDtoTotalSum;
    }

    //find all dates from expenses
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

        TreeSet<LocalDate> localDateSet = new TreeSet<>();
            for (Expense expense : expenseList) {
                localDateSet.add(expense.getDateOfExpense());
        }

        return localDateSet;
    }
}
