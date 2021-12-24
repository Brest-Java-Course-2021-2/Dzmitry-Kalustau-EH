package com.epam.brest.service.rest;

import com.epam.brest.model.Category;
import com.epam.brest.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoriesServiceRest implements CategoryService {

    private final Logger logger = LogManager.getLogger(CategoriesServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public CategoriesServiceRest() {
    }

    public CategoriesServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Category> findAllCategories() {

        logger.debug("findAllCategories()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Category>) responseEntity.getBody();
    }

    @Override
    public Category getCategoryById(Integer categoryId) {

        logger.debug("getCategoryById = {}", categoryId);
        ResponseEntity<Category> responseEntity =
                restTemplate.getForEntity(url + "/" + categoryId, Category.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer create(Category category) {

        logger.debug("create() {}", category);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, category, Integer.class);
        return (Integer) responseEntity.getBody();
    }

    @Override
    public Integer update(Category category) {

        logger.debug("update() {}", category);
        // restTemplate.put(url, category);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Category> entity = new HttpEntity<>(category, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }



    @Override
    public Integer delete(Integer categoryId) {

        logger.debug("delete() {}", categoryId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Integer> entity = new HttpEntity<>(categoryId, headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url, HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();

    }

    @Override
    public Integer count() {

        logger.debug("count()");
        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(url + "/count" , Integer.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer getIdOfLastCategory() {

        logger.debug("get id of last category");
        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(url + "/last_id", Integer.class);
        return responseEntity.getBody();
    }
}
