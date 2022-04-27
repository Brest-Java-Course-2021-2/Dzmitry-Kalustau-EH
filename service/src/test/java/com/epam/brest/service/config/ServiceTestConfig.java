package com.epam.brest.service.config;

import com.epam.brest.dao.CategoryDao;
import com.epam.brest.dao.CategoryDaoJDBCImpl;
import com.epam.brest.dao.ExpenseDao;
import com.epam.brest.dao.ExpenseDaoJDBCImpl;
import com.epam.brest.dao.dto.CalculateSumDtoDao;
import com.epam.brest.dao.dto.CalculateSumDtoDaoJdbc;
import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ExpenseService;
import com.epam.brest.service.dto.CalculateSumDtoService;
import com.epam.brest.service.impl.CategoryServiceImpl;
import com.epam.brest.service.impl.ExpenseServiceImpl;
import com.epam.brest.service.impl.dto.CalculateSumDtoServiceImpl;
import com.epam.brest.testdb.SpringTestJdbcConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfig extends SpringTestJdbcConfig {

    @Bean
    CategoryDao categoryDao() {
        return new CategoryDaoJDBCImpl(namedParameterJdbcTemplate());
    }

    @Bean
    CategoryService categoryService() { return new CategoryServiceImpl(categoryDao());
    }

    @Bean
    ExpenseDao expenseDao() {
        return new ExpenseDaoJDBCImpl(namedParameterJdbcTemplate());
    }

    @Bean
    ExpenseService expenseService() {
        return new ExpenseServiceImpl(expenseDao());
    }

    @Bean
    CalculateSumDtoDao calculateSumDtoDao() {
        return new CalculateSumDtoDaoJdbc(namedParameterJdbcTemplate());
    }

    @Bean
    CalculateSumDtoService calculateSumDtoService() {
        return new CalculateSumDtoServiceImpl(calculateSumDtoDao());
    }
}
