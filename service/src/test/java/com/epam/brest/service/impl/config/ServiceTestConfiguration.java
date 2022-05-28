package com.epam.brest.service.impl.config;

import com.epam.brest.dao.dto.CalculateSumDtoDao;
import com.epam.brest.dao.dto.CalculateSumDtoDaoJdbc;
import com.epam.brest.service.dto.CalculateSumDtoService;
import com.epam.brest.service.impl.dto.CalculateSumDtoServiceImpl;
import com.epam.brest.testdb.SpringTestJdbcConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

// For integration tests
@TestConfiguration
public class ServiceTestConfiguration extends SpringTestJdbcConfig {

    @Bean
    CalculateSumDtoDao calculateSumDtoDao() {
        return new CalculateSumDtoDaoJdbc(namedParameterJdbcTemplate());
    }

    @Bean
    CalculateSumDtoService calculateSumDtoService() {
        return new CalculateSumDtoServiceImpl(calculateSumDtoDao());
    }
}
