package com.epam.brest.web_app;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
@Transactional
public class CategoriesControllerIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldUpdateCategoryAfterEdit() throws Exception {

        String testName = RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/edit-categories/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("categoryId", "1")
                        .param("categoryName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        Category category = categoryService.getCategoryById(1);
        assertNotNull(category);
        assertEquals(testName, category.getCategoryName());
    }



}
