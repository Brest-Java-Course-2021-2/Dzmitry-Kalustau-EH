//package com.epam.brest.service.rest;
//
//
//import com.epam.brest.model.Category;
//import com.epam.brest.service.config.ServiceRestTestConfig;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.client.ExpectedCount;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
//
//@ExtendWith(SpringExtension.class)
//@Import({ServiceRestTestConfig.class})
//public class CategoriesServiceRestTest {
//
//    private final Logger LOGGER = LogManager.getLogger(CategoriesServiceRestTest.class);
//
//    public static final String CATEGORIES_URL = "http://localhost:8088/categories";
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    private MockRestServiceServer mockServer;
//
//    private ObjectMapper mapper = new ObjectMapper();
//
//    CategoriesServiceRest categoriesService;
//
//    @BeforeEach
//    public void before() {
//        mockServer = MockRestServiceServer.createServer(restTemplate);
//        categoriesService = new CategoriesServiceRest(CATEGORIES_URL, restTemplate);
//    }
//
//    @Test
//    public void testFindAllCategories() throws Exception {
//
//        LOGGER.debug("test FindAllCategories()");
//        // given
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
//                );
//
//        // when
//        List<Category> list = categoriesService.findAllCategories();
//
//        // then
//        mockServer.verify();
//        assertNotNull(list);
//        assertTrue(list.size() > 0);
//    }
//
//    @Test
//    public void testCreateCategory() throws Exception {
//
//        LOGGER.debug("test CreateCategory()");
//        // given
//        Category category = new Category();
//                category.setCategoryName(RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
//
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString("1"))
//                );
//        // when
//        Integer id = categoriesService.create(category);
//
//        // then
//        mockServer.verify();
//        assertNotNull(id);
//    }
//
//    @Test
//    public void testGetCategoryById() throws Exception {
//
//        // given
//        Integer id = 1;
//        Category category = new Category();
//                category.setCategoryId(id);
//                category.setCategoryName(RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
//
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(category))
//                );
//
//        // when
//        Category resultCategory = categoriesService.getCategoryById(id);
//
//        // then
//        mockServer.verify();
//        assertNotNull(resultCategory);
//        assertEquals(resultCategory.getCategoryId(), id);
//        assertEquals(resultCategory.getCategoryName(), category.getCategoryName());
//    }
//
//    @Test
//    public void testUpdateCategory() throws Exception {
//
//        // given
//        Integer id = 1;
//        Category category = new Category();
//                category.setCategoryId(id);
//                category.setCategoryName(RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
//
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
//                .andExpect(method(HttpMethod.PUT))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString("1"))
//                );
//
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(category))
//                );
//
//        // when
//        int result = categoriesService.update(category);
//        Category updatedCategory = categoriesService.getCategoryById(id);
//
//        // then
//        mockServer.verify();
//        assertTrue(1 == result);
//
//        assertNotNull(updatedCategory);
//        assertEquals(updatedCategory.getCategoryId(), id);
//        assertEquals(updatedCategory.getCategoryName(), category.getCategoryName());
//    }
//
//    @Test
//    public void testDeleteCategory() throws Exception {
//
//        // given
//        Integer id = 1;
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
//                .andExpect(method(HttpMethod.DELETE))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString("1"))
//                );
//        // when
//        int result = categoriesService.delete(id);
//
//        // then
//        mockServer.verify();
//        assertTrue(1 == result);
//    }
//
//    private Category create(int index) {
//        Category category = new Category();
//        category.setCategoryId(index);
//        category.setCategoryName("c" + index);
//        return category;
//    }
//}
//
