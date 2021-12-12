package com.epam.brest.service.rest;

import com.epam.brest.model.Category;
import com.epam.brest.model.Expense;
import com.epam.brest.model.exceptions.IncorrectExpense;
import com.epam.brest.service.ExpenseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ExpensesServiceRest implements ExpenseService {

    private final Logger logger = LogManager.getLogger(ExpensesServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public ExpensesServiceRest() {
    }

    public ExpensesServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Expense> findAllExpenses() {

        logger.debug("findAllExpenses()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Expense>) responseEntity.getBody();
    }

    @Override
    public Expense getExpenseById(Integer expenseId) {

        logger.debug("getExpenseById = {}", expenseId);
        ResponseEntity<Expense> responseEntity =
                restTemplate.getForEntity(url + "/" + expenseId, Expense.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer create(Expense expense) throws IncorrectExpense {
        return null;
    }

    @Override
    public Integer update(Expense expense) {

        logger.debug("update() {}", expense);
        // restTemplate.put(url, expense);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Expense> entity = new HttpEntity<>(expense, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    @Override
    public Integer delete(Integer expenseId) {
        return null;
    }
}
