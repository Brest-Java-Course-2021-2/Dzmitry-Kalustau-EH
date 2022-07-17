package com.epam.brest.rest;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.model.dto.ReportDto;
import com.epam.brest.service.dto.CalculateSumDtoService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import java.util.List;


@RestController
@CrossOrigin
@Api(value = "calculatesum", tags = "Calculate Sum API")
public class CalculateSumController {

    private static final Logger logger = LogManager.getLogger(CalculateSumController.class);

    private CalculateSumDtoService calculateSumDtoService;

    @Autowired
    public CalculateSumController(CalculateSumDtoService calculateSumDtoService) {
        this.calculateSumDtoService = calculateSumDtoService;
    }

    @GetMapping("/calculate-sum")
    @ApiOperation(value = "Get list with sum of expenses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final Collection<CalculateSumDto> getSumOfExpenses() {

        logger.debug("get sum of expenses()");
        List<CalculateSumDto> calculateSumDtoList = calculateSumDtoService.findAllWithSumOfExpenses();
        return calculateSumDtoList;
    }

    @GetMapping("/calculate-sum/dates")
    @ApiOperation(value = "Get LocalDate Container")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final LocalDateContainer getLocalDateContainer() {

        logger.debug("get localDate Container");
        LocalDateContainer localDateContainer = calculateSumDtoService.getLocalDateContainer();
        return localDateContainer;
    }

    @PostMapping(path = "calculate-sum", consumes = "application/json")
    @ApiOperation(value = "Add Dates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Error")
    }
    )
    public void addDates(@ApiParam(name = "LocalDate Container", value = "Container with date from and date to") @RequestBody LocalDateContainer localDateContainer) {

        logger.debug("add dates ({})", localDateContainer);
        calculateSumDtoService.editLocalDateContainer(localDateContainer.getDateFrom(), localDateContainer.getDateTo());
    }

    @GetMapping("/calculate-sum/totalsum")
    @ApiOperation(value = "Get Total Sum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "The resource not found")
    }
    )
    public final CalculateSumDto getCalculateSumDtoTotalSum() {

        logger.debug("get CalculateSumDto Total Sum");
        CalculateSumDto calculateSumDtoTotalSum = calculateSumDtoService.getTotalSum();
        return calculateSumDtoTotalSum;
    }

    @PostMapping(path = "calculate-sum/report", consumes = "application/json")
    public ReportDto createReport(@RequestBody Integer months) {

        logger.debug("create report for {} months", months);
        return calculateSumDtoService.createReport(months);
    }

}
