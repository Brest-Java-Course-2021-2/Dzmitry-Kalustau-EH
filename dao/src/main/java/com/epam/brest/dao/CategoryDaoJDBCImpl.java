package com.epam.brest.dao;

import com.epam.brest.model.Category;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class CategoryDaoJDBCImpl implements CategoryDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CategoryDaoJDBCImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public Integer create(Category category) {
        return null;
    }

    @Override
    public Integer update(Category category) {
        return null;
    }

    @Override
    public Integer delete(Integer categoryId) {
        return null;
    }
}