package com.epam.brest.web_app;

import com.epam.brest.model.dto.CalculateSumDto;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Disabled
class CalculateSumControllerTest {

    private static final String CALCULATESUM_DTOS_URL = "http://localhost:8088/calculate-sum";

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

//    @Test
//    void testReturnCalculateSumPage() throws Exception {
//
//        CalculateSumDto c1 = createCalculateSumDto("Food", BigDecimal.valueOf(150));
//        CalculateSumDto c2 = createCalculateSumDto("Households", BigDecimal.valueOf(400));
//        CalculateSumDto c3 = createCalculateSumDto("Car", null);
//        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CALCULATESUM_DTOS_URL)))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(Arrays.asList(c1, c2, c3)))
//                );
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/calculate-sum")
//                ).andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("calculate-sum"))
//                .andExpect(model().attribute("CalculateSum", hasItem(
//                        allOf(
//                                hasProperty("categoryName", is(c1.getCategoryName())),
//                                hasProperty("sumOfExpense", is(c1.getSumOfExpense()))
//                        )
//                )))
////                .andExpect(model().attribute("departments", hasItem(
////                        allOf(
////                                hasProperty("departmentId", is(d2.getDepartmentId())),
////                                hasProperty("departmentName", is(d2.getDepartmentName())),
////                                hasProperty("avgSalary", is(d2.getAvgSalary()))
////                        )
////                )))
////                .andExpect(model().attribute("departments", hasItem(
////                        allOf(
////                                hasProperty("departmentId", is(d3.getDepartmentId())),
////                                hasProperty("departmentName", is(d3.getDepartmentName())),
////                                hasProperty("avgSalary", isEmptyOrNullString())
////                        )
////                )))
//        ;
//
//        mockServer.verify();
//    }

    private CalculateSumDto createCalculateSumDto(String categoryName, BigDecimal sumOfExpense) {
        CalculateSumDto calculateSumDto = new CalculateSumDto();
        calculateSumDto.setCategoryName(categoryName);
        calculateSumDto.setSumOfExpense(sumOfExpense);
        return calculateSumDto;
    }

}

