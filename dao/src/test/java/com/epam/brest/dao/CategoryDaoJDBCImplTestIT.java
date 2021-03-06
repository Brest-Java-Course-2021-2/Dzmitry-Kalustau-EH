package com.epam.brest.dao;

import com.epam.brest.model.Category;
import com.epam.brest.testdb.SpringTestJdbcConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({CategoryDaoJDBCImpl.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringTestJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
@ActiveProfiles("dev")
class CategoryDaoJDBCImplTestIT {

    private final Logger logger = LogManager.getLogger(CategoryDaoJDBCImplTestIT.class);

    private CategoryDaoJDBCImpl categoryDaoJDBC;

    public CategoryDaoJDBCImplTestIT(@Autowired CategoryDao categoryDaoJDBC) {
        this.categoryDaoJDBC = (CategoryDaoJDBCImpl) categoryDaoJDBC;
    }

    @Test
    void testFindAll() {

        logger.debug("Execute IT test: findAll()");
        assertNotNull(categoryDaoJDBC);
        assertNotNull(categoryDaoJDBC.findAllCategories());
    }

    @Test
    void testCreate() {

        logger.debug("Execute IT test create()");
        assertNotNull(categoryDaoJDBC);
        int categorySizeBefore = categoryDaoJDBC.count();
        Category category = new Category(7, "Tickets");
        Integer categoryId = categoryDaoJDBC.create(category);
        assertNotNull(categoryId);
        assertEquals(categorySizeBefore, categoryDaoJDBC.count() - 1);
    }

    @Test
    void testCreateEqualsCategory() {

        logger.debug("Execute IT test createEqualsCategory()");
        assertNotNull(categoryDaoJDBC);
        Category category = new Category(7, "Restaurant");
        assertThrows(IllegalArgumentException.class, () -> {
            categoryDaoJDBC.create(category);
            categoryDaoJDBC.create(category);
        });
    }

    @Test
    void testCount() {

        logger.debug("Execute IT test count()");
        assertNotNull(categoryDaoJDBC);
        Integer countCategory = categoryDaoJDBC.count();
        assertNotNull(countCategory);
        assertTrue(countCategory > 0);
        assertEquals(Integer.valueOf(6), countCategory);
    }

    @Test
    void testGetIdOfLastCategory() {

        logger.debug("Execute IT test getIdOfLastCategory()");
        assertNotNull(categoryDaoJDBC);

        List<Category> categoryList = categoryDaoJDBC.findAllCategories();
        Category categoryBeforeAdd = categoryList.get(categoryList.size() - 1);

        categoryDaoJDBC.create(new Category(7,"Toys"));
        Integer idOfLastCategoryAfterAdd = categoryDaoJDBC.getIdOfLastCategory();
        assertNotNull(idOfLastCategoryAfterAdd);
        assertEquals(categoryBeforeAdd.getCategoryId() + 2 , idOfLastCategoryAfterAdd);
    }

    @Test
    void testGetCategoryByID() {

        logger.debug("Execute IT test getCategoryById()");
        List<Category> categoryList = categoryDaoJDBC.findAllCategories();
        if (categoryList.size() == 0) {
            categoryList.add(new Category("Test Category"));
        }
        Category categoryFromList = categoryList.get(0);
        Category categoryFromDao = categoryDaoJDBC.getCategoryById(categoryFromList.getCategoryId());
        assertEquals(categoryFromList.getCategoryName(), categoryFromDao.getCategoryName());
    }

    @Test
    void testUpdate() {

        logger.debug("Execute IT test update()");
        List<Category> categoryList = categoryDaoJDBC.findAllCategories();
        if (categoryList.size() == 0) {
            categoryDaoJDBC.create(new Category("Test Category"));
            categoryList = categoryDaoJDBC.findAllCategories();
        }

        Category categoryFromList = categoryList.get(0);
        categoryFromList.setCategoryName(categoryFromList.getCategoryName() + "_test");
        categoryDaoJDBC.update(categoryFromList);

        Category categoryFromDao = categoryDaoJDBC.getCategoryById(categoryFromList.getCategoryId());
        assertEquals(categoryFromList.getCategoryName(), categoryFromDao.getCategoryName());
    }

    @Test
    void testDelete() {

        logger.debug("Execute IT test delete()");
        categoryDaoJDBC.create(new Category(7, "Test Category"));
        List<Category> categoryList = categoryDaoJDBC.findAllCategories();

        categoryDaoJDBC.delete(categoryList.get(categoryList.size()-1).getCategoryId());
        assertEquals(categoryList.size()-1, categoryDaoJDBC.findAllCategories().size());
    }
}