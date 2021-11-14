package com.epam.brest.dao;

import com.epam.brest.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-jdbc-conf.xml"})
class CategoryDaoJDBCImplTestIT {

    private final Logger logger = LogManager.getLogger(CategoryDaoJDBCImpl.class);

    private CategoryDaoJDBCImpl categoryDaoJDBC;

    public CategoryDaoJDBCImplTestIT(@Autowired CategoryDao categoryDaoJDBC) {
        this.categoryDaoJDBC = (CategoryDaoJDBCImpl) categoryDaoJDBC;
    }

    @Test
    void findAll() {
        logger.debug("Execute test: findAll()");
        assertNotNull(categoryDaoJDBC);
        assertNotNull(categoryDaoJDBC.findAll());
    }

    @Test
    void create() {
        assertNotNull(categoryDaoJDBC);
        int categorySizeBefore = categoryDaoJDBC.findAll().size();
        Category category = new Category("Tickets");
        Integer categoryId = categoryDaoJDBC.create(category);
        assertNotNull(categoryId);
        assertEquals(categorySizeBefore, categoryDaoJDBC.findAll().size() - 1);
    }

    @Test
    void tryToCreateEqualsCategory() {
        assertNotNull(categoryDaoJDBC);
        Category category = new Category("Restaurant");
        assertThrows(DuplicateKeyException.class, () -> {
            categoryDaoJDBC.create(category);
            categoryDaoJDBC.create(category);
        });
    }
}