package com.epam.brest.rest;

import com.epam.brest.model.Category;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import java.util.List;


@RestController
@CrossOrigin
public class CalculateSumController {

    private static final Logger logger = LogManager.getLogger(CalculateSumController.class);

    private CalculateSumDtoService calculateSumDtoService;;

    @Autowired
    public CalculateSumController(CalculateSumDtoService calculateSumDtoService) {
        this.calculateSumDtoService = calculateSumDtoService;
    }

    //TODO make Dates
    @GetMapping("/calculate-sum")
    public final Collection<CalculateSumDto> getSumOfExpenses() {

        logger.debug("get sum of expenses()");
        List<CalculateSumDto> calculateSumDtoList = calculateSumDtoService.findAllWithSumOfExpenses();
        return calculateSumDtoList;
    }

    @GetMapping("/calculate-sum/dates")
    public final LocalDateContainer getLocalDateContainer() {

        logger.debug("get localDate Container");
        LocalDateContainer localDateContainer = calculateSumDtoService.getLocalDateContainer();
        return localDateContainer;
    }

    @PostMapping(path = "calculate-sum", consumes = "application/json")
    public void addDates(@RequestBody LocalDateContainer localDateContainer) {

        logger.debug("add dates ({})", localDateContainer);
        calculateSumDtoService.editLocalDateContainer(localDateContainer.getDateFrom(), localDateContainer.getDateTo());
    }

    @GetMapping("/calculate-sum/totalsum")
    public final CalculateSumDto getCalculateSumDtoTotalSum() {

        logger.debug("get CalculateSumDto Total Sum");
        CalculateSumDto calculateSumDtoTotalSum = calculateSumDtoService.getTotalSum();
        return calculateSumDtoTotalSum;
    }

}
