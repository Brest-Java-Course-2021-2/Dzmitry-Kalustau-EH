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
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryDaoJDBCImpl implements CategoryDao {

    private final Logger logger = LogManager.getLogger(CategoryDaoJDBCImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_ALL_CATEGORIES = "SELECT c.category_id, c.category_name FROM category c ORDER BY c.category_id";
    private final String SQL_CREATE_CATEGORY = "INSERT INTO category(category_id, category_name) VALUES (:categoryId, :categoryName)";
    private final String SQL_CHECK_UNIQUE_CATEGORY_NAME = "SELECT count(c.category_name) FROM category c WHERE lower(c.category_name) = lower(:categoryName)";
    private final String SQL_SELECT_COUNT = "SELECT count(*) FROM category";
    private final String SQL_UPDATE_CATEGORY = "UPDATE category SET category_name = :categoryName WHERE category_id = :categoryId";
    private final String SQL_CATEGORY_BY_ID = "SELECT c.category_id, c.category_name FROM category c WHERE c.category_id = :categoryId";
    private final String SQL_DELETE_CATEGORY_BY_ID = "DELETE FROM category WHERE category_id = :categoryId";

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

        Map<String, Object> paramsOfSql = new HashMap<>();
        paramsOfSql.put("categoryName", category.getCategoryName());
        paramsOfSql.put("categoryId", category.getCategoryId());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramsOfSql);
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
    public Category getCategoryById(Integer categoryId) {
        logger.debug("Get category by id = {}", categoryId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("categoryId", categoryId);
        return namedParameterJdbcTemplate.queryForObject(SQL_CATEGORY_BY_ID, sqlParameterSource, new CategoryRowMapper());

    }

    @Override
    public Integer update(Category category) {
        logger.debug("Update category {}", category);

        Map<String, Object> paramsOfSql = new HashMap<>();
        paramsOfSql.put("categoryName", category.getCategoryName());
        paramsOfSql.put("categoryId", category.getCategoryId());

        return namedParameterJdbcTemplate.update(SQL_UPDATE_CATEGORY, paramsOfSql);
    }

    @Override
    public Integer delete(Integer categoryId) {
        logger.debug("Delete category on id {}", categoryId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("categoryId", categoryId);
        return namedParameterJdbcTemplate.update(SQL_DELETE_CATEGORY_BY_ID, sqlParameterSource);
    }

    @Override
    public Integer count() {
        logger.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(SQL_SELECT_COUNT, new MapSqlParameterSource(), Integer.class);
    }

    @Override
    public Integer getIdOfLastCategory() {
        logger.debug("getIdOfLastCategory");

        List<Category> categoryList = namedParameterJdbcTemplate.query(SQL_ALL_CATEGORIES, new CategoryRowMapper());
        Integer idOfLastCategory = 1;
        if (categoryList.isEmpty()) {
            return idOfLastCategory;
        }

        idOfLastCategory = findMissedId(categoryList);
        return idOfLastCategory;
        }


    private Integer findMissedId(List<Category> categoryList) {
        Integer missedId = categoryList.size()+1;
        List<Integer> categoryIdList = categoryList.stream().map((category) -> category.getCategoryId()).collect(Collectors.toList());
        Integer[] numbersArray = new Integer[categoryIdList.size()];
        for (int i=0; i<numbersArray.length; i++) numbersArray[i] = i + 1;

        for (int i=0; i<categoryIdList.size(); i++) {
            if (categoryIdList.get(i) != numbersArray[i]) {
                missedId = categoryIdList.get(i-1) + 1;
                break;
            }
        }
        return missedId;
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
