package com.epam.brest.service.rest;

import com.epam.brest.model.Category;
import com.epam.brest.service.config.ServiceRestTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@Import({ServiceRestTestConfig.class})
public class CategoriesServiceRestTest {

    private final Logger LOGGER = LogManager.getLogger(CategoriesServiceRestTest.class);

    public static final String CATEGORIES_URL = "http://localhost:8088/categories";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    CategoriesServiceRest categoriesService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        categoriesService = new CategoriesServiceRest(CATEGORIES_URL, restTemplate);
    }

    private Category create(int index) {
        Category category = new Category();
        category.setCategoryId(index);
        category.setCategoryName("c" + index);
        return category;
    }
}

