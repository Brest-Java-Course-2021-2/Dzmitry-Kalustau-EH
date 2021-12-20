package com.epam.brest.service.rest;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalculateSumDtoServiceRest implements CalculateSumDtoService {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public CalculateSumDtoServiceRest() {
    }

    public CalculateSumDtoServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CalculateSumDto> findAllWithSumOfExpenses() {

        logger.debug("findAllWithSumOfExpenses()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<CalculateSumDto>) responseEntity.getBody();
    }

    @Override
    public LocalDateContainer getLocalDateContainer() {

        logger.debug("getLocalDateContainer()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url + "/dates", LocalDateContainer.class);
        return (LocalDateContainer) responseEntity.getBody();
    }

    @Override
    public void editLocalDateContainer(LocalDate localDateFrom, LocalDate localDateTo) {

        logger.debug("editLocalDateContainer() with String dates {}, {}", localDateFrom, localDateTo);
        LocalDateContainer localDateContainer = new LocalDateContainer(localDateFrom, localDateTo);
        restTemplate.postForEntity(url, localDateContainer, Integer.class);
    }

}
