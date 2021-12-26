package com.epam.brest.service.rest;

import com.epam.brest.model.Expense;
import com.epam.brest.service.config.ServiceRestTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@Import({ServiceRestTestConfig.class})
class ExpensesServiceRestTest {

    private final Logger logger = LogManager.getLogger(ExpensesServiceRestTest.class);

    public static final String EXPENSES_URL = "http://localhost:8088/expenses";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    ExpensesServiceRest expensesServiceRest;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        expensesServiceRest = new ExpensesServiceRest(EXPENSES_URL, restTemplate);
        mapper.findAndRegisterModules();
    }

    @Test
    public void testFindAllExpenses() throws Exception {

        logger.debug("test FindAllExpenses()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(createExpense(0), createExpense(1))))
                );

        // when
        List<Expense> expenseList = expensesServiceRest.findAllExpenses();

        // then
        mockServer.verify();
        assertNotNull(expenseList);
        assertTrue(expenseList.size() > 0);
    }

    @Test
    public void testCreate() throws Exception {

        logger.debug("test Create()");
        // given
        Expense expense = createExpense(1);


        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = expensesServiceRest.create(expense);

        // then
        mockServer.verify();
        assertNotNull(id);
    }
//
    @Test
    public void testGetExpenseById() throws Exception {

        // given
        Integer id = 1;
        Expense expense = createExpense(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expense))
                );

        // when
        Expense resultExpense = expensesServiceRest.getExpenseById(id);

        // then
        mockServer.verify();
        assertNotNull(resultExpense);
        assertEquals(resultExpense.getExpenseId(), id);
        assertEquals(resultExpense.getSumOfExpense(), expense.getSumOfExpense());
    }

    @Test
    public void testUpdate() throws Exception {

        // given
        Integer id = 1;
        Expense expense = createExpense(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expense))
                );

        // when
        int result = expensesServiceRest.update(expense);
        Expense updatedExpense = expensesServiceRest.getExpenseById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertNotNull(updatedExpense);
        assertEquals(updatedExpense.getExpenseId(), id);
        assertEquals(updatedExpense.getSumOfExpense(), expense.getSumOfExpense());
    }
//
//    @Test
//    public void shouldDeleteDepartment() throws Exception {
//
//        // given
//        Integer id = 1;
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL + "/" + id)))
//                .andExpect(method(HttpMethod.DELETE))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString("1"))
//                );
//        // when
//        int result = expensesServiceRest.delete(id);
//
//        // then
//        mockServer.verify();
//        assertTrue(1 == result);
//    }

    private Expense createExpense(int index) {
        Expense expense = new Expense(index, LocalDate.now().minusDays(1), 1, BigDecimal.valueOf(12));
        return expense;
    }
}

