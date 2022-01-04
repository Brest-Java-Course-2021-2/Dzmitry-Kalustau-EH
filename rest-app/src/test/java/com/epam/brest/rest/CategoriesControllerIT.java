package com.epam.brest.rest;

import com.epam.brest.model.Category;
import com.epam.brest.rest.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class CategoriesControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesControllerIT.class);

    public static final String CATEGORIES_ENDPOINT = "/categories";

    @Autowired
    private CategoriesController categoriesController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    MockMvcCategoriesService categoriesService = new MockMvcCategoriesService();

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoriesController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void testFindAllCategories() throws Exception {

        // given
        Category category = new Category(7, RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
        Integer id = categoriesService.create(category);

        // when
        List<Category> categories = categoriesService.findAllCategories();

        // then
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
    }

    @Test
    public void testCreateCategory() throws Exception {
        Category category = new Category(7, RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
        Integer id = categoriesService.create(category);
        assertNotNull(id);
    }

    @Test
    public void testFindCategoryById() throws Exception {

        // given
        Category category = new Category(7, RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
        Integer id = categoriesService.create(category);

        assertNotNull(id);

        // when
        Optional<Category> optionalCategory = categoriesService.getCategoryById(id);

        // then
        assertTrue(optionalCategory.isPresent());
        assertEquals(optionalCategory.get().getCategoryId(), id);
        assertEquals(category.getCategoryName(), optionalCategory.get().getCategoryName());
    }

    @Test
    public void testGetIdOfLastCategory() throws Exception {

        // given
        List<Category> categoryList = categoriesService.findAllCategories();
        Integer idOfLastCategoryBeforeAdd = categoryList.get(categoryList.size()-1).getCategoryId();
        assertNotNull(idOfLastCategoryBeforeAdd);

        categoriesService.create(new Category(7, "Toys"));

        // when
        Integer idOfLastCategory = categoriesService.getIdOfLastCategory();

        // then
        assertNotNull(idOfLastCategory);
        assertEquals(idOfLastCategory, idOfLastCategoryBeforeAdd + 2);
    }

    @Test
    public void testUpdateCategory() throws Exception {

        // given
        Category category = new Category(7, RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
        Integer id = categoriesService.create(category);
        assertNotNull(id);

        Optional<Category> categoryOptional = categoriesService.getCategoryById(id);
        assertTrue(categoryOptional.isPresent());

        categoryOptional.get().
                setCategoryName(RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));

        // when
        int result = categoriesService.update(categoryOptional.get());

        // then
        assertTrue(1 == result);

        Optional<Category> updatedCategoryOptional = categoriesService.getCategoryById(id);
        assertTrue(updatedCategoryOptional.isPresent());
        assertEquals(updatedCategoryOptional.get().getCategoryId(), id);
        assertEquals(updatedCategoryOptional.get().getCategoryName(), categoryOptional.get().getCategoryName());

    }

    @Test
    public void testDeleteCategory() throws Exception {
        // given
        Category category = new Category(7, RandomStringUtils.randomAlphabetic(CATEGORY_NAME_SIZE));
        Integer id = categoriesService.create(category);

        List<Category> categories = categoriesService.findAllCategories();
        assertNotNull(categories);

        // when
        int result = categoriesService.delete(id);

        // then
        assertTrue(1 == result);

        List<Category> currentCategories = categoriesService.findAllCategories();
        assertNotNull(currentCategories);

        assertTrue(categories.size() - 1 == currentCategories.size());
    }



    class MockMvcCategoriesService {

        public List<Category> findAllCategories() throws Exception {

            LOGGER.debug("findAllCategories()");
            MockHttpServletResponse response = mockMvc.perform(get(CATEGORIES_ENDPOINT)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Category>>() {
            });
        }

        public Optional<Category> getCategoryById(Integer id) throws Exception {

            LOGGER.debug("getCategoryById{})", id);
            MockHttpServletResponse response = mockMvc.perform(get(CATEGORIES_ENDPOINT + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Category.class));
        }

        public Integer getIdOfLastCategory() throws Exception {

            LOGGER.debug("getIdOfLastCategory()");
            MockHttpServletResponse response = mockMvc.perform(get(CATEGORIES_ENDPOINT + "/last_id")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer create(Category category) throws Exception {

            LOGGER.debug("create({})", category);
            String json = objectMapper.writeValueAsString(category);
            MockHttpServletResponse response =
                    mockMvc.perform(post(CATEGORIES_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int update(Category category) throws Exception {

            LOGGER.debug("update({})", category);
            MockHttpServletResponse response =
                    mockMvc.perform(put(CATEGORIES_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(category))
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        private int delete(Integer categoryId) throws Exception {

            LOGGER.debug("delete(id:{})", categoryId);
            MockHttpServletResponse response = mockMvc.perform(
                            MockMvcRequestBuilders.delete(CATEGORIES_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(categoryId))
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}
