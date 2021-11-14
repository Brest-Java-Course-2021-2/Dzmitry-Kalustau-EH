package com.epam.brest.web_app;

import com.epam.brest.service.dto.CalculateSumDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalculateSumController {

    private CalculateSumDtoService calculateSumDtoService;

    @Autowired
    public CalculateSumController(CalculateSumDtoService calculateSumDtoService) {
        this.calculateSumDtoService = calculateSumDtoService;
    }

    @GetMapping("/calculate-sum")
    public String calculateSum(Model model) {
        model.addAttribute("CalculateSum", calculateSumDtoService.findAllWithSumOfExpenses());



        return "calculate-sum";
    }

}
