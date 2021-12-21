package com.epam.brest.rest;

import com.epam.brest.model.Expense;
import com.epam.brest.rest.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class ExpensesControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpensesControllerIT.class);

    private static Integer categoryId;

    public static final String EXPENSES_ENDPOINT = "/expenses";

    @Autowired
    private ExpensesController expensesController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    MockMvcExpensesService expensesService = new MockMvcExpensesService();

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(expensesController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();

        Random random = new Random(1);
        categoryId = random.nextInt(5)+1;

    }

    @Test
    public void testFindAllExpenses() throws Exception {

        // given
        Expense expense = new Expense();
        expense.setCategoryId(categoryId);
        Integer id = expensesService.create(expense);

        // when
        List<Expense> expenses = expensesService.findAllExpenses();

        // then
        assertNotNull(expenses);
        assertTrue(expenses.size() > 0);
    }

    @Test
    public void testCreateExpense() throws Exception {
        Expense expense = new Expense();
        expense.setCategoryId(categoryId);
        Integer id = expensesService.create(expense);
        assertNotNull(id);
    }

    @Test
    public void testFindExpenseById() throws Exception {

        // given
        Expense expense = new Expense();
        expense.setCategoryId(categoryId);
        Integer id = expensesService.create(expense);

        assertNotNull(id);

        // when
        Optional<Expense> optionalExpense = expensesService.getExpenseById(id);

        // then
        assertTrue(optionalExpense.isPresent());
        assertEquals(optionalExpense.get().getCategoryId(), id);
        assertEquals(expense.getExpenseId(), optionalExpense.get().getExpenseId());
    }

    @Test
    public void testUpdateExpense() throws Exception {

        // given
        Expense expense = new Expense(new BigDecimal("22.5"));
        expense.setCategoryId(categoryId);
        Integer id = expensesService.create(expense);
        assertNotNull(id);

        Optional<Expense> expenseOptional = expensesService.getExpenseById(id);
        assertTrue(expenseOptional.isPresent());

        expenseOptional.get().
                setSumOfExpense(new BigDecimal("15"));

        // when
        int result = expensesService.update(expenseOptional.get());

        // then
        assertTrue(1 == result);

        Optional<Expense> updatedExpenseOptional = expensesService.getExpenseById(id);
        assertTrue(updatedExpenseOptional.isPresent());
        assertEquals(updatedExpenseOptional.get().getCategoryId(), id);
        assertEquals(updatedExpenseOptional.get().getSumOfExpense(), expenseOptional.get().getSumOfExpense());

    }

    @Test
    public void testDeleteExpense() throws Exception {
        // given
        Expense expense = new Expense(categoryId);
        expense.setCategoryId(categoryId);
        Integer id = expensesService.create(expense);

        List<Expense> expenses = expensesService.findAllExpenses();
        assertNotNull(expenses);

        // when
        int result = expensesService.delete(id);

        // then
        assertTrue(1 == result);

        List<Expense> currentExpenses = expensesService.findAllExpenses();
        assertNotNull(currentExpenses);

        assertTrue(expenses.size() - 1 == currentExpenses.size());
    }


    class MockMvcExpensesService {

        public List<Expense> findAllExpenses() throws Exception {

            LOGGER.debug("findAllExpenses()");
            MockHttpServletResponse response = mockMvc.perform(get(EXPENSES_ENDPOINT)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Expense>>() {
            });
        }

        public Optional<Expense> getExpenseById(Integer id) throws Exception {

            LOGGER.debug("getExpenseById{})", id);
            MockHttpServletResponse response = mockMvc.perform(get(EXPENSES_ENDPOINT + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Expense.class));
        }

        public Integer create(Expense expense) throws Exception {

            LOGGER.debug("create({})", expense);
            String json = objectMapper.writeValueAsString(expense);
            MockHttpServletResponse response =
                    mockMvc.perform(post(EXPENSES_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int update(Expense expense) throws Exception {

            LOGGER.debug("update({})", expense);
            MockHttpServletResponse response =
                    mockMvc.perform(put(EXPENSES_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(expense))
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int delete(Integer expenseId) throws Exception {

            LOGGER.debug("delete(id:{})", expenseId);
            MockHttpServletResponse response = mockMvc.perform(
                            MockMvcRequestBuilders.delete(EXPENSES_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(expenseId))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}
