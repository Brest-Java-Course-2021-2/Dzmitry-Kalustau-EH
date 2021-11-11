package com.epam.brest.dao;

import com.epam.brest.model.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDaoJDBCImpl implements CategoryDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_ALL_CATEGORIES = "SELECT c.category_id, c.category_name FROM category c ORDER BY c.category_name";
    private final String SQL_CREATE_CATEGORY = "INSERT INTO category(category_name) VALUES (:category_name)";

    public CategoryDaoJDBCImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Category> findAll() {

        return namedParameterJdbcTemplate.query(SQL_ALL_CATEGORIES, new CategoryRowMapper());
    }

    @Override
    public Integer create(Category category) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("category_name", category.getCategoryName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_CATEGORY, sqlParameterSource, keyHolder);
        return (Integer) keyHolder.getKey();
    }

    @Override
    public Integer update(Category category) {
        return null;
    }

    @Override
    public Integer delete(Integer categoryId) {
        return null;
    }

    private class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet resultSet, int i) throws SQLException {
            Category category = new Category();
            category.setCategoryId(resultSet.getInt("category_id"));
            category.setCategoryName(resultSet.getString("category_name"));
            return category;
        }
    }
}
