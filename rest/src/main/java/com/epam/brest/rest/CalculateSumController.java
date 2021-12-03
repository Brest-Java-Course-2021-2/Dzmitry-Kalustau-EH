package com.epam.brest.rest;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

import java.util.List;


@Controller
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
        LocalDateContainer localDateContainer = calculateSumDtoService.getLocalDateContainer();

        List<CalculateSumDto> calculateSumDtoList = calculateSumDtoService.findAllWithSumOfExpenses();

        return calculateSumDtoList;
    }

    // TODO make Post for dates

}
