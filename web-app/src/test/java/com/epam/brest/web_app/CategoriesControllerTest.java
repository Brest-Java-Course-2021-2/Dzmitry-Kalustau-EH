package com.epam.brest.web_app;

import com.epam.brest.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;

import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Disabled
class CategoriesControllerTest {

    private static final String CATEGORIES_URL = "http://localhost:8088/categories";

    private static final Logger logger = LogManager.getLogger(CategoriesControllerTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testReturnCategoriesPage() throws Exception {

        Category category1 = createCategory(1, "Food");
        Category category2 = createCategory(2, "Households");
        Category category3 = createCategory(3, "Car");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(category1, category2, category3)))
                );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("categories"));

        mockServer.verify();
    }

    @Test
    public void testOpenAddCategoryPage() throws Exception {

        logger.debug("test OpenAddCategoryPage");
        Integer lastId = 7;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/last_id")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(lastId))
                );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/add-categories")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-categories"))
                .andExpect(model().attribute("category", hasProperty("categoryId", is(7))));
    }

    @Test
    void testAddCategory() throws Exception {

        logger.debug("test AddCategory()");
        // WHEN
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        Category category = new Category("Fuel");

        // THEN
        //Integer newCategoryId = categoryService.create(category);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/add-categories")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryName", category.getCategoryName())
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));


        // VERIFY
        mockServer.verify();
    }

    @Test
    public void testOpenEditCategoryPageById() throws Exception {

        logger.debug("test OpenEditCategoryPageById");
        Category category = createCategory(7, "Fuel");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + category.getCategoryId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/edit-categories/7")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("edit-categories"))
                .andExpect(model().attribute("category", hasProperty("categoryId", is(7))))
                .andExpect(model().attribute("category", hasProperty("categoryName", is("Fuel"))));
    }

    @Test
    public void testUpdateCategoryAfterEdit() throws Exception {

        logger.debug("test UpdateCategoryAfterEdit()");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        String testName = RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/edit-categories/1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("categoryName", testName)
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        mockServer.verify();
    }

    @Test
    public void testOpenDeleteCategoryPage() throws Exception {

        logger.debug("test OpenDeleteCategoryPage()");
        Category category = createCategory(7, "Fuel");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + category.getCategoryId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/delete-categories/7")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("delete-categories"))
                .andExpect(model().attribute("category", hasProperty("categoryId", is(7))))
                .andExpect(model().attribute("category", hasProperty("categoryName", is("Fuel"))));
    }

    @Test
    public void testDeleteCategory() throws Exception {

        logger.debug("test DeleteCategory");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        String testName = RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/delete-categories/1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("categoryName", testName)
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        mockServer.verify();
    }

    private Category createCategory(int id, String name) {

        logger.info("create category with id={}, name={}", id, name);
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        return category;
    }
}

