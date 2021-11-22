package com.epam.brest.dao;

import com.epam.brest.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CategoryDaoJDBCImplTest {

    @InjectMocks
    private CategoryDaoJDBCImpl categoryDaoJDBC;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testFindAllCategories() {
        Category category = new Category();
        List<Category> list = Collections.singletonList(category);

        Mockito.when(namedParameterJdbcTemplate.query(any(), ArgumentMatchers.<RowMapper<Category>>any())).thenReturn(list);
        List<Category> result = categoryDaoJDBC.findAllCategories();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertSame(category, list.get(0));
    }


}
