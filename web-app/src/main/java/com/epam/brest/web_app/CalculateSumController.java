package com.epam.brest.web_app;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class CalculateSumController {

    private static final Logger logger = LogManager.getLogger(CalculateSumController.class);

    private CalculateSumDtoService calculateSumDtoService;;

    @Autowired
    public CalculateSumController(CalculateSumDtoService calculateSumDtoService) {
        this.calculateSumDtoService = calculateSumDtoService;
    }

    @GetMapping("/calculate-sum")
    public String gotoCalculateSum(Model model) {

        logger.debug("calculate sum getMapping");

        LocalDateContainer localDateContainer = calculateSumDtoService.getLocalDateContainer();
        model.addAttribute("localDate", localDateContainer);

        List<CalculateSumDto> calculateSumDtoList = calculateSumDtoService.findAllWithSumOfExpenses();
        model.addAttribute("CalculateSum", calculateSumDtoList);

        return "calculate-sum";
    }

    @PostMapping("/calculate-sum")
    public String calculateSum(LocalDateContainer localDate) {

        logger.debug("calculate sum postMapping");

        calculateSumDtoService.editLocalDateContainer(localDate.getLocalDateFrom(), localDate.getLocalDateTo());

        return "redirect:/calculate-sum";
    }

}
