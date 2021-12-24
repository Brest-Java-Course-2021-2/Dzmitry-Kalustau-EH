package com.epam.brest.web_app;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.epam.brest.model.constants.CategoryConstants.CATEGORY_NAME_SIZE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Disabled
class CalculateSumControllerTest {

    private static final String CALCULATESUM_URL = "http://localhost:8088/calculate-sum";

    private static final Logger logger = LogManager.getLogger(CalculateSumControllerTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    public void testUpdateDatesAfterEdit() throws Exception {

        logger.debug("test UpdateDatesAfterEdit()");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CALCULATESUM_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        LocalDate testLocalDateFrom = LocalDate.of(2021, 1, 1);
        LocalDate testLocalDateTo = LocalDate.of(2021, 1, 2);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/calculate-sum")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("localDateFrom", testLocalDateFrom.toString())
                                .param("localDateTo", testLocalDateTo.toString())
                ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/calculate-sum"))
                .andExpect(redirectedUrl("/calculate-sum"));;

        mockServer.verify();
    }

    private CalculateSumDto createCalculateSumDto(String categoryName, BigDecimal sumOfExpense) {
        CalculateSumDto calculateSumDto = new CalculateSumDto();
        calculateSumDto.setCategoryName(categoryName);
        calculateSumDto.setSumOfExpense(sumOfExpense);
        return calculateSumDto;
    }


}

