package com.epam.brest.service.rest;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.service.config.ServiceRestTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@Import({ServiceRestTestConfig.class})
class CalculateSumDtoServiceRestTest {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoServiceRestTest.class);

    public static final String URL = "http://localhost:8088/calculateSum-dtos";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    CalculateSumDtoServiceRest calculateSumDtoServiceRest;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        calculateSumDtoServiceRest = new CalculateSumDtoServiceRest(URL, restTemplate);
    }

    @Test
    void testFindAllWithSumOfExpenses() throws Exception {

        logger.debug("test FindAllWithSumOfExpenses()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(create(0), create(1))))
                );

        // when
        List<CalculateSumDto> list = calculateSumDtoServiceRest.findAllWithSumOfExpenses();

        // then
        mockServer.verify();
        assertNotNull(list);
        assertTrue(list.size() > 0);

    }

    private CalculateSumDto create(int index) {
        CalculateSumDto calculateSumDto = new CalculateSumDto();
        calculateSumDto.setCategoryName("Category number " + index);
        calculateSumDto.setSumOfExpense(BigDecimal.valueOf(100 + index));
        return calculateSumDto;
    }
}
