package com.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalculateSumController {

    @GetMapping("/calculate-sum")
    public String calculateSum() {
        return "calculate-sum";
    }

}
