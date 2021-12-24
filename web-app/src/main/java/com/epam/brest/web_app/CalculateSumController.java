package com.epam.brest.web_app;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        CalculateSumDto calculateSumDtoTotalSum = calculateSumDtoService.getTotalSum();
        model.addAttribute("totalSum", calculateSumDtoTotalSum);

        return "calculate-sum";
    }

    @PostMapping("/calculate-sum")
    public String calculateSum(LocalDateContainer localDate) {

        logger.debug("calculate sum postMapping");

        calculateSumDtoService.editLocalDateContainer(localDate.getDateFrom(), localDate.getDateTo());

        return "redirect:/calculate-sum";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException{
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            @Override
            public String getAsText() throws IllegalArgumentException {
                return DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) getValue());
            }
        });
    }

}
