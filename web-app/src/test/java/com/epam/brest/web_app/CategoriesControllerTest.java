package com.epam.brest.web_app;

import com.epam.brest.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
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

//    @Test
//    void testReturnCategoriesPage() throws Exception {
//
//        Category c1 = createCategory(1, "Food");
//        Category c2 = createCategory(2, "Households");
//        Category c3 = createCategory(3, "Car");
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(Arrays.asList(c1, c2, c3)))
//                );
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/categories")
//                ).andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("categories"))
//                .andExpect(model().attribute("categories", hasItem(
//                        allOf(
//                                hasProperty("categoryId", is(c1.getCategoryId())),
//                                hasProperty("categoryName", is(c1.getCategoryName()))
//                        )
//                )))
////                .andExpect(model().attribute("categories", hasItem(
////                        allOf(
////                                hasProperty("departmentId", is(c2.getCategoryId())),
////                                hasProperty("departmentName", is(c2.getCategoryName()))
////                        )
////                )))
////                .andExpect(model().attribute("categories", hasItem(
////                        allOf(
////                                hasProperty("departmentId", is(c3.getCategoryId())),
////                                hasProperty("departmentName", is(c3.getCategoryName()))
////                        )
////                )))
//        ;
//
//        mockServer.verify();
//    }

    @Test
    void testAddCategory() throws Exception {
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

//    @Test
//    void testFailAddCategoryOnEmptyName() throws Exception {
//        // WHEN
//        Category category = new Category("");
//
//        // THEN
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/add-categories")
//                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                                .param("categoryName", category.getCategoryName())
//                ).andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(view().name("add-categories"))
//                .andExpect(
//                        model().attributeHasFieldErrors(
//                                "add-categories", "categoryName"
//                        )
//                );
//    }

//    @Test
//    public void testOpenEditCategoryPageById() throws Exception {
//        Category category = createCategory(1, "Food");
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + category.getCategoryId())))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(category))
//                );
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/edit-categories/1")
//                ).andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("edit-categories"))
//                .andExpect(model().attribute("edit-categories", hasProperty("categoryId", is(1))))
//                .andExpect(model().attribute("edit-categories", hasProperty("categoryName", is("Food"))));
//    }

    @Test
    public void testUpdateCategoryAfterEdit() throws Exception {

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

//    @Test
//    public void testDeleteCategory() throws Exception {
//
//        int id = 6;
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
//                .andExpect(method(HttpMethod.DELETE))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body("1")
//                );
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/delete-categories/6")
//                ).andExpect(status().isOk())
//                .andExpect(view().name("redirect:/categories"))
//                .andExpect(redirectedUrl("/categories"));
//
//        mockServer.verify();
//    }

    private Category createCategory(int id, String name) {
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        return category;
    }
}

