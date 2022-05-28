package com.epam.brest.rest;

import com.epam.brest.model.Expense;
import com.epam.brest.rest.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
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
@PropertySource({"classpath:dao.properties"})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
public class ExpensesControllerIT {

    private static final Logger logger = LoggerFactory.getLogger(ExpensesControllerIT.class);

    private Expense testExpense;

    public static final String EXPENSES_ENDPOINT = "/expenses";

    @Autowired
    private ExpensesController expensesController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    MockMvcExpensesService expensesService = new MockMvcExpensesService();

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(expensesController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        Random random = new Random(1);
        testExpense = new Expense(9);
        testExpense.setCategoryId(random.nextInt(5)+1);
        testExpense.setSumOfExpense( new BigDecimal(random.nextDouble()*10));
    }

    @Test
    public void testCreateExpense() throws Exception {

        logger.debug("test CreateExpense()");
        Integer id = expensesService.create(testExpense);
        assertNotNull(id);
    }

    @Test
    public void testFindAllExpenses() throws Exception {

        logger.debug("test FindAllExpenses()");
        // given
        Integer id = expensesService.create(testExpense);

        // when
        List<Expense> expenses = expensesService.findAllExpenses();

        // then
        assertNotNull(expenses);
        assertTrue(expenses.size() > 0);
    }

    @Test
    public void testFindExpenseById() throws Exception {

        logger.debug("test FindExpenseById");
        // given
        Integer id = expensesService.create(testExpense);

        assertNotNull(id);

        // when
        Optional<Expense> optionalExpense = expensesService.getExpenseById(id);

        // then
        assertTrue(optionalExpense.isPresent());
        assertEquals(optionalExpense.get().getExpenseId(), id);
        assertEquals(testExpense.getSumOfExpense(), optionalExpense.get().getSumOfExpense());
    }

    @Test
    public void testGetIdOfLastExpense() throws Exception {

        // given
        List<Expense> expenseList = expensesService.findAllExpenses();
        Integer idOfLastExpenseBeforeAdd = expenseList.get(expenseList.size()-1).getExpenseId();
        assertNotNull(idOfLastExpenseBeforeAdd);

        expensesService.create(new Expense(9, LocalDate.now(), 1, BigDecimal.valueOf(2)));

        // when
        Integer idOfLastExpense = expensesService.getIdOfLastExpense();

        // then
        assertNotNull(idOfLastExpense);
        assertEquals(idOfLastExpense, idOfLastExpenseBeforeAdd + 2);
    }

    @Test
    public void testUpdateExpense() throws Exception {

        logger.debug("test UpdateExpense");
        // given
        Integer id = expensesService.create(testExpense);
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
        assertEquals(updatedExpenseOptional.get().getExpenseId(), id);
        assertEquals(updatedExpenseOptional.get().getSumOfExpense(), expenseOptional.get().getSumOfExpense());

    }

    @Test
    public void testDeleteExpense() throws Exception {

        logger.debug("test DeleteExpense");
        // given
        Integer id = expensesService.create(testExpense);

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

            logger.debug("findAllExpenses()");
            MockHttpServletResponse response = mockMvc.perform(get(EXPENSES_ENDPOINT)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);


            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Expense>>() {
            });
        }

        public Optional<Expense> getExpenseById(Integer id) throws Exception {

            logger.debug("getExpenseById{})", id);
            MockHttpServletResponse response = mockMvc.perform(get(EXPENSES_ENDPOINT + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Expense.class));
        }

        public Integer getIdOfLastExpense() throws Exception {

            logger.debug("getIdOfLastExpense()");
            MockHttpServletResponse response = mockMvc.perform(get(EXPENSES_ENDPOINT + "/last_id")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer create(Expense expense) throws Exception {

            logger.debug("create({})", expense);
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

            logger.debug("update({})", expense);
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

            logger.debug("delete(id:{})", expenseId);
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
