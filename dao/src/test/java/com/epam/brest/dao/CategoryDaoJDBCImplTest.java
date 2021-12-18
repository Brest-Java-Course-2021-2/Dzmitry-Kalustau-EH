//package com.epam.brest.dao;
//
//import com.epam.brest.model.Category;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//
//@ExtendWith(MockitoExtension.class)
//public class CategoryDaoJDBCImplTest {
//
//    private final Logger logger = LogManager.getLogger(CategoryDaoJDBCImplTest.class);
//
//    @InjectMocks
//    private CategoryDaoJDBCImpl categoryDaoJDBC;
//
//    @Mock
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    @Captor
//    private ArgumentCaptor<RowMapper<Category>> captorMapper;
//    @Captor
//    private ArgumentCaptor<SqlParameterSource> captorSource;
//
//    @AfterEach
//    public void check() {
//        Mockito.verifyNoMoreInteractions(namedParameterJdbcTemplate);
//    }
//
//    @Test
//    public void testFindAllCategories() {
//        logger.debug("Execute test findAllCategories()");
//
//        String sql = "select";
//        ReflectionTestUtils.setField(categoryDaoJDBC, "SQL_ALL_CATEGORIES", sql);
//        Category category = new Category();
//        List<Category> list = Collections.singletonList(category);
//
//        Mockito.when(namedParameterJdbcTemplate.query(any(), ArgumentMatchers.<RowMapper<Category>>any()))
//                .thenReturn(list);
//
//        List<Category> result = categoryDaoJDBC.findAllCategories();
//
//        Mockito.verify(namedParameterJdbcTemplate).query(any(), captorMapper.capture());
//
//        RowMapper<Category> mapper = captorMapper.getValue();
//
//        Assertions.assertNotNull(mapper);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//        Assertions.assertSame(category, result.get(0));
//    }
//
//    @Test
//    public void testGetCategoryById() {
//        logger.debug("Execute test getCategoryById()");
//
//        String sql = "get";
//        ReflectionTestUtils.setField(categoryDaoJDBC, "SQL_CATEGORY_BY_ID", sql);
//        int id = 0;
//        Category category = new Category();
//
//        Mockito.when(namedParameterJdbcTemplate.queryForObject(
//                any(),
//                ArgumentMatchers.<SqlParameterSource>any(),
//                ArgumentMatchers.<RowMapper<Category>>any())
//        ).thenReturn(category);
//
//        Category result = categoryDaoJDBC.getCategoryById(id);
//
//        Mockito.verify(namedParameterJdbcTemplate)
//                .queryForObject(any(), captorSource.capture(), captorMapper.capture());
//
//        SqlParameterSource source = captorSource.getValue();
//        RowMapper<Category> mapper = captorMapper.getValue();
//
//        Assertions.assertNotNull(source);
//        Assertions.assertNotNull(mapper);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertSame(category, result);
//    }
//
//
//}
