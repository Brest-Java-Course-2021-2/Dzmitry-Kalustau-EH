package com.epam.brest.web_app.config;

import com.epam.brest.service.CategoryService;
import com.epam.brest.service.ExpenseService;
import com.epam.brest.service.dto.CalculateSumDtoService;
import com.epam.brest.service.rest.CalculateSumDtoServiceRest;
import com.epam.brest.service.rest.CategoriesServiceRest;
import com.epam.brest.service.rest.ExpensesServiceRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class ApplicationConfig {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    CategoryService categoryService() {
        String url = String.format("%s://%s:%d/categories", protocol, host, port);
        return new CategoriesServiceRest(url, restTemplate());
    }

    @Bean
    ExpenseService expenseService() {
        String url = String.format("%s://%s:%d/expenses", protocol, host, port);
        return new ExpensesServiceRest(url, restTemplate());
    }

    @Bean
    CalculateSumDtoService calculateSumDtoService() {
        String url = String.format("%s://%s:%d/calculate-sum", protocol, host, port);
        return new CalculateSumDtoServiceRest(url, restTemplate());
    }
}
