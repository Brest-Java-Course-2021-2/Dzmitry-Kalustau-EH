package com.epam.brest.dao.dto;

import com.epam.brest.model.dto.CategoryDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CategoryDtoDaoJdbc implements CategoryDtoDao {

    private final String SQL_FIND_ALL_WITH_SUM_OF_EXPENSES = "SELECT\n" +
            "\tc.category_name AS categoryName,\n" +
            "\tsum(e.price) AS sumOfExpense\n" +
            "FROM\n" +
            "\texpense e\n" +
            "INNER JOIN category c ON\n" +
            "\te.category_id = c.category_id\n" +
            "WHERE date BETWEEN\n" +
            "\t:dateFrom AND :dateTo\n" +
            "GROUP BY\n" +
            "\te.category_id";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CategoryDtoDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<CategoryDto> findAllWithSumOfExpenses() {
        Map<String, LocalDate> paramsOfSQL = new HashMap<>();

        // temporary solution!!
        paramsOfSQL.put("dateFrom", LocalDate.of(2021, 10, 1));
        paramsOfSQL.put("dateTo",  LocalDate.of(2021, 10, 3));

        List<CategoryDto> categoriesDto = namedParameterJdbcTemplate.query(SQL_FIND_ALL_WITH_SUM_OF_EXPENSES,
        paramsOfSQL, BeanPropertyRowMapper.newInstance(CategoryDto.class));
        return categoriesDto;
    }
}
