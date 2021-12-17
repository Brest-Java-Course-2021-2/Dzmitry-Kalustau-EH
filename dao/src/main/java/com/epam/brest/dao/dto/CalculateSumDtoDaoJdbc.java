package com.epam.brest.dao.dto;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CalculateSumDtoDaoJdbc implements CalculateSumDtoDao {

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

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private LocalDateContainer localDateContainer = new LocalDateContainer(LocalDate.of( 2021, 01, 01), LocalDate.of( 2021, 12, 29));

    public CalculateSumDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<CalculateSumDto> findAllWithSumOfExpenses()  {

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

        public List<CalculateSumDto> findSumOfExpensesBetweenDates(String localDateFrom, String localDateTo) {
        Map<String, String> paramsOfSQL = new HashMap<>();

        paramsOfSQL.put("dateFrom", localDateFrom);
        paramsOfSQL.put("dateTo",  localDateTo);

        List<CalculateSumDto> calculateSumDtoList = namedParameterJdbcTemplate.query(SQL_FIND_SUM_OF_EXPENSES_BETWEEN_DATES,
                paramsOfSQL, BeanPropertyRowMapper.newInstance(CalculateSumDto.class));
        addTotalSum(calculateSumDtoList);
        return calculateSumDtoList;

    }


    //Add final Sum of all expenses (Total Sum in the table)
    private List<CalculateSumDto> addTotalSum(List<CalculateSumDto> calculateSumDtoList) {
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
        localDateContainer.setDateFrom(localDateFrom);
        localDateContainer.setDateTo(localDateTo);
    }
}
