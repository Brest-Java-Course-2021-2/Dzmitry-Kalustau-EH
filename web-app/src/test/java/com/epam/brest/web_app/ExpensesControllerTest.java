package com.epam.brest.web_app;

import com.epam.brest.model.Category;
import com.epam.brest.model.Expense;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Disabled
class ExpensesControllerTest {

    private static final String EXPENSES_URL = "http://localhost:8088/expenses";

    private static final Logger logger = LogManager.getLogger(ExpensesControllerTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    void testAddExpense() throws Exception {

        logger.debug("test AddExpense() ");
        // WHEN
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        Expense expense = new Expense(1, LocalDate.of(2021, 1, 1), 2, new BigDecimal(5));

        // THEN

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/add-expenses")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("sumOfExpense", expense.getSumOfExpense().toString())
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/expenses"))
                .andExpect(redirectedUrl("/expenses"));


        // VERIFY
        mockServer.verify();
    }


    @Test
    public void testOpenEditExpensePageById() throws Exception {

        logger.debug("test OpenEditExpensePageById()");
        Expense expense = new Expense(1);
        expense.setCategoryId(2);
        expense.setSumOfExpense(new BigDecimal(5));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL + "/" + expense.getExpenseId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expense))
                );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/edit-expenses/1")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("edit-expenses"))
                .andExpect(model().attribute("expense", hasProperty("expenseId", is(1))))
                .andExpect(model().attribute("expense", hasProperty("categoryId", is(2))))
                .andExpect(model().attribute("expense", hasProperty("sumOfExpense", is(new BigDecimal(5)))));
    }

    @Test
    public void testUpdateExpenseAfterEdit() throws Exception {

        logger.debug("test UpdateExpenseAfterEdit()");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
       BigDecimal testSumOfExpense = new BigDecimal(Math.random());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/edit-expenses/1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("expenseId", "1")
                                .param("sumOfExpense", testSumOfExpense.toString())
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/expenses"))
                .andExpect(redirectedUrl("/expenses"));

        mockServer.verify();
    }

    @Test
    public void testOpenDeleteExpensePage() throws Exception {

        logger.debug("test OpenDeleteExpensePage()");
        Expense expense = new Expense(1);
        expense.setCategoryId(2);
        expense.setSumOfExpense(new BigDecimal(5));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL + "/" + expense.getExpenseId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expense))
                );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/delete-expenses/1")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("delete-expenses"))
                .andExpect(model().attribute("expense", hasProperty("expenseId", is(1))))
                .andExpect(model().attribute("expense", hasProperty("categoryId", is(2))))
                .andExpect(model().attribute("expense", hasProperty("sumOfExpense", is(new BigDecimal(5)))));
    }

    @Test
    public void testDeleteExpense() throws Exception {

        logger.debug("test DeleteExpense");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EXPENSES_URL)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        BigDecimal testSumOfExpense = new BigDecimal(Math.random());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/delete-expenses/1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("expenseId", "1")
                                .param("sumOfExpense", testSumOfExpense.toString())
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/expenses"))
                .andExpect(redirectedUrl("/expenses"));

        mockServer.verify();
    }

}

