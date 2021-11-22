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
    void shouldAddCategory() throws Exception {
        // WHEN
        assertNotNull(categoryService);
        Integer categoriesSizeBefore = categoryService.count();
        assertNotNull(categoriesSizeBefore);
        Category category = new Category("Tickets");

        // THEN
        mockMvc.perform(
                MockMvcRequestBuilders.post("/edit-categories")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("categoryName", category.getCategoryName())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        // VERIFY
        assertEquals(categoriesSizeBefore, categoryService.count() - 1);
    }

    @Test
    public void shouldOpenEditCategoryPageById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/edit-categories/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("edit-categories"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("category", hasProperty("categoryId", is(1))))
                .andExpect(model().attribute("category", hasProperty("categoryName", is("Food"))));
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

    @Test
    public void shouldDeleteCategory() throws Exception {

        Integer countBefore = categoryService.count();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/edit-categories/6/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        // verify database size
        Integer countAfter = categoryService.count();
        assertEquals(countBefore - 1, countAfter);
    }

}
