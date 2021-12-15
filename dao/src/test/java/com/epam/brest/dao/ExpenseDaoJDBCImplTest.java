//package com.epam.brest.dao;
//
//import com.epam.brest.model.Category;
//import com.epam.brest.model.Expense;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//public class ExpenseDaoJDBCImplTest {
//
//    private final Logger logger = LogManager.getLogger(ExpenseDaoJDBCImplTest.class);
//
//    @InjectMocks
//    private ExpenseDaoJDBCImpl expenseDaoJDBC;
//
//    @Mock
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    @Captor
//    private ArgumentCaptor<RowMapper<Expense>> captorMapper;
//
//    @Captor
//    private ArgumentCaptor<SqlParameterSource> captorSource;
//
//    @Test
//    void testFindAllExpenses() {
//        logger.debug("Execute test findAllExpenses()");
//
//        Expense expense = new Expense();
//        List<Expense> list = Collections.singletonList(expense);
//
//        Mockito.when(namedParameterJdbcTemplate.query(any(), ArgumentMatchers.<RowMapper<Expense>>any()))
//                .thenReturn(list);
//
//        List<Expense> result = expenseDaoJDBC.findAllExpenses();
//
//        Mockito.verify(namedParameterJdbcTemplate).query(any(), captorMapper.capture());
//
//        RowMapper<Expense> mapper = captorMapper.getValue();
//
//        Assertions.assertNotNull(mapper);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//        Assertions.assertSame(expense, result.get(0));
//    }
//
//    @Test
//    void testGetExpenseById() {
//        logger.debug("Execute test getExpenseById()");
//
//        Expense expense = new Expense();
//
//        Mockito.when(namedParameterJdbcTemplate.queryForObject(
//                any(),
//                ArgumentMatchers.<SqlParameterSource>any(),
//                ArgumentMatchers.<RowMapper<Expense>>any())
//        ).thenReturn(expense);
//
//        Expense result = expenseDaoJDBC.getExpenseById(0);
//
//        Mockito.verify(namedParameterJdbcTemplate)
//                .queryForObject(any(), captorSource.capture(), captorMapper.capture());
//
//        SqlParameterSource source = captorSource.getValue();
//        RowMapper<Expense> mapper = captorMapper.getValue();
//
//        Assertions.assertNotNull(source);
//        Assertions.assertNotNull(mapper);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertSame(expense, result);
//    }
//}
