package com.epam.brest.service.rest;

import com.epam.brest.model.Category;
import com.epam.brest.service.config.ServiceRestTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@Import({ServiceRestTestConfig.class})
class CategoriesServiceRestTest {

    private final Logger logger = LogManager.getLogger(CategoriesServiceRestTest.class);

    public static final String CATEGORIES_URL = "http://localhost:8088/categories";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    CategoriesServiceRest categoriesServiceRest;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        categoriesServiceRest = new CategoriesServiceRest(CATEGORIES_URL, restTemplate);
        mapper.findAndRegisterModules();
    }

    @Test
    public void testFindAllCategories() throws Exception {

        logger.debug("test FindAllCategories()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(createCategory(0), createCategory(1))))
                );

        // when
        List<Category> categoryList = categoriesServiceRest.findAllCategories();

        // then
        mockServer.verify();
        assertNotNull(categoryList);
        assertTrue(categoryList.size() > 0);
    }

    @Test
    public void testCreate() throws Exception {

        logger.debug("test Create()");
        // given
        Category category = createCategory(1);


        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = categoriesServiceRest.create(category);

        // then
        mockServer.verify();
        assertNotNull(id);
    }
    //
    @Test
    public void testGetCategoryById() throws Exception {

        logger.debug("test GetCategoryById()");
        // given
        Integer id = 1;
        Category category = createCategory(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );

        // when
        Category resultCategory = categoriesServiceRest.getCategoryById(id);

        // then
        mockServer.verify();
        assertNotNull(resultCategory);
        assertEquals(resultCategory.getCategoryId(), id);
        assertEquals(resultCategory.getCategoryName(), category.getCategoryName());
    }

    @Test
    public void testUpdate() throws Exception {

        logger.debug("test Update()");
        // given
        Integer id = 1;
        Category category = createCategory(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );

        // when
        int result = categoriesServiceRest.update(category);
        Category updatedCategory = categoriesServiceRest.getCategoryById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertNotNull(updatedCategory);
        assertEquals(updatedCategory.getCategoryId(), id);
        assertEquals(updatedCategory.getCategoryName(), category.getCategoryName());
    }

    @Test
    public void testDelete() throws Exception {

        logger.debug("test Delete()");
        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        int result = categoriesServiceRest.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    @Test
    public void testCount() throws Exception {

        logger.debug("test Count()");
        // given
        Integer count = 8;

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/count")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(count))
                );

        // when
        Integer countCategories = categoriesServiceRest.count();

        // then
        mockServer.verify();
        assertNotNull(countCategories);
        assertEquals(countCategories, 8);
    }

    @Test
    public void testGetIdOfLastCategory() throws Exception {

        logger.debug("test GetIdOfLastCategory()");
        // given
        Integer id = 1;

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/last_id")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(id))
                );

        // when
        Integer idOfLastCategory = categoriesServiceRest.getIdOfLastCategory();

        // then
        mockServer.verify();
        assertNotNull(idOfLastCategory);
        assertEquals(idOfLastCategory, id);
    }

    private Category createCategory(int index) {
        Category category = new Category(index, "TestCategory");
        return category;
    }
}

