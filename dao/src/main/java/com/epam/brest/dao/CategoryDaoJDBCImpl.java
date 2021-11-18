package com.epam.brest.dao;

import com.epam.brest.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final Logger logger = LogManager.getLogger(CategoryDaoJDBCImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_ALL_CATEGORIES = "SELECT c.category_id, c.category_name FROM category c ORDER BY c.category_id";
    private final String SQL_CREATE_CATEGORY = "INSERT INTO category(category_name) VALUES (:category_name)";
    private final String SQL_CHECK_UNIQUE_CATEGORY_NAME = "SELECT count(c.category_name) FROM category c WHERE lower(c.category_name) = lower(:categoryName)";
    private final String SQL_SELECT_COUNT = "SELECT count(*) FROM category";

    public CategoryDaoJDBCImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public CategoryDaoJDBCImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Category> findAllCategories() {
        logger.debug("Start: findAllCategories()");
        return namedParameterJdbcTemplate.query(SQL_ALL_CATEGORIES, new CategoryRowMapper());
    }

    @Override
    public Integer create(Category category) {
        logger.debug("Start: create({})", category);

        if (!isCategoryUnique(category.getCategoryName())) {
            logger.warn("Category with the same name {} already exists", category.getCategoryName());
            throw new IllegalArgumentException("Category with the same name already exists in DB.");
        }

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("category_name", category.getCategoryName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_CATEGORY, sqlParameterSource, keyHolder);
        return (Integer) keyHolder.getKey();
    }

    private boolean isCategoryUnique(String categoryName) {
        logger.debug("Check categoryName : {} on unique", categoryName);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("categoryName", categoryName);
        return namedParameterJdbcTemplate.queryForObject(SQL_CHECK_UNIQUE_CATEGORY_NAME, sqlParameterSource, Integer.class) == 0;
    }

    @Override
    public Integer update(Category category) {
        return null;
    }

    @Override
    public Integer delete(Integer categoryId) {
        return null;
    }

    @Override
    public Integer count() {
        logger.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(SQL_SELECT_COUNT, new MapSqlParameterSource(), Integer.class);
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
