package com.epam.brest.dao;

import com.epam.brest.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CategoryDaoJDBCImpl implements CategoryDao {

    private final Logger logger = LogManager.getLogger(CategoryDaoJDBCImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${SQL_ALL_CATEGORIES}")
    public String sqlAllCategories;
    @Value("${SQL_CREATE_CATEGORY}")
    public String sqlCreateCategory;
    @Value("${SQL_CHECK_UNIQUE_CATEGORY_NAME}")
    public String sqlCheckUniqueCategoryName;
    @Value("${SQL_SELECT_COUNT}")
    public String sqlSelectCount;
    @Value("${SQL_UPDATE_CATEGORY}")
    public String sqlUpdateCategory;
    @Value("${SQL_CATEGORY_BY_ID}")
    public String sqlCategoryById;
    @Value("${SQL_DELETE_CATEGORY_BY_ID}")
    public String sqlDeleteCategoryById;

    public CategoryDaoJDBCImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Category> findAllCategories() {
        logger.debug("Start: findAllCategories()");
        return namedParameterJdbcTemplate.query(sqlAllCategories, new CategoryRowMapper());
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
        namedParameterJdbcTemplate.update(sqlCreateCategory, sqlParameterSource, keyHolder);
        return (Integer) keyHolder.getKey();
    }

    private boolean isCategoryUnique(String categoryName) {
        logger.debug("Check categoryName : {} on unique", categoryName);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("categoryName", categoryName);
        return namedParameterJdbcTemplate.queryForObject(sqlCheckUniqueCategoryName, sqlParameterSource, Integer.class) == 0;
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        logger.debug("Get category by id = {}", categoryId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("categoryId", categoryId);
        return namedParameterJdbcTemplate.queryForObject(sqlCategoryById, sqlParameterSource, new CategoryRowMapper());

    }

    @Override
    public Integer update(Category category) {
        logger.debug("Update category {}", category);

        Map<String, Object> paramsOfSql = new HashMap<>();
        paramsOfSql.put("categoryName", category.getCategoryName());
        paramsOfSql.put("categoryId", category.getCategoryId());

        return namedParameterJdbcTemplate.update(sqlUpdateCategory, paramsOfSql);
    }

    @Override
    public Integer delete(Integer categoryId) {
        logger.debug("Delete category on id {}", categoryId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("categoryId", categoryId);
        return namedParameterJdbcTemplate.update(sqlDeleteCategoryById, sqlParameterSource);
    }

    @Override
    public Integer count() {
        logger.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(sqlSelectCount, new MapSqlParameterSource(), Integer.class);
    }

    @Override
    public Integer getIdOfLastCategory() {
        logger.debug("getIdOfLastCategory");

        List<Category> categoryList = namedParameterJdbcTemplate.query(sqlAllCategories, new CategoryRowMapper());
        Integer idOfLastCategory = 1;
        if (categoryList.isEmpty()) {
            logger.info("categoryList was empty");
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

        if (categoryIdList.get(0) != numbersArray[0]) {
            return 1;
        }

        for (int i=1; i<categoryIdList.size(); i++) {
            if (categoryIdList.get(i) != numbersArray[i]) {
                return categoryIdList.get(i-1) + 1;
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
