package com.epam.brest.rest;

import com.epam.brest.model.Category;
import com.epam.brest.rest.exception.CustomExceptionHandler;
import com.epam.brest.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

    @InjectMocks
    private CategoriesController categoriesController;

    @Mock
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Integer> captorId;

    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoriesController)
                .setControllerAdvice(new CustomExceptionHandler())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void getCategoryById() throws Exception {
        Category category = new Category();
        category.setCategoryId(15);
        category.setCategoryName("Category");

        Mockito.when(categoryService.getCategoryById(anyInt())).thenReturn(category);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/categories/123")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId", Matchers.is(category.getCategoryId())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.categoryName", Matchers.is(category.getCategoryName())))
        ;

        Mockito.verify(categoryService).getCategoryById(captorId.capture());

        Integer id = captorId.getValue();
        Assertions.assertEquals(123, id);
    }

    @Test
    public void getCategoryByIdException() throws Exception {

        Mockito.when(categoryService.getCategoryById(anyInt()))
                .thenThrow(new IllegalArgumentException("test message"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/categories/123")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Handle: test message"))
        ;
    }
}