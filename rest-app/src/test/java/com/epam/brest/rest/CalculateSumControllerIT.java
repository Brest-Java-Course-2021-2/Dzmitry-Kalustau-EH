package com.epam.brest.rest;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.rest.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@PropertySource({"classpath:dao.properties"})
@AutoConfigureMockMvc
@Transactional
public class CalculateSumControllerIT {

    private static final Logger logger = LoggerFactory.getLogger(CalculateSumControllerIT.class);

    public static final String CALCULATESUM_ENDPOINT = "/calculate-sum";

    private CalculateSumDto testCalculateSumDto;

    @Autowired
    private CalculateSumController calculateSumController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    MockMvcCalculateSumService calculateSumService = new MockMvcCalculateSumService();

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(calculateSumController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        Random random = new Random(1);
        testCalculateSumDto = new CalculateSumDto("Food", BigDecimal.valueOf(random.nextDouble() * 10));
    }

    @Test
    public void testGetSumOfExpenses() throws Exception {

        // when
        List<CalculateSumDto> calculateSumDtos = calculateSumService.findAllWithSumOfExpenses();

        // then
        assertNotNull(calculateSumDtos);
        assertTrue(calculateSumDtos.size() > 0);
    }

    @Test
    public void testGetLocalDateContainer() throws Exception {

        // when
        Optional<LocalDateContainer> optionalLocalDateContainer = calculateSumService.getLocalDateContainer();

        // then
        assertTrue(optionalLocalDateContainer.isPresent());
        assertNotNull(optionalLocalDateContainer.get().getDateFrom());
        assertNotNull(optionalLocalDateContainer.get().getDateTo());
    }

    @Test
    public void testGetCalculateSumDtoTotalSum() throws Exception {

        // when
        Optional<CalculateSumDto> optionalCalculateSumDtoTotalSum = calculateSumService.getTotalSum();

        // then
        assertTrue(optionalCalculateSumDtoTotalSum.isPresent());
        assertNotNull(optionalCalculateSumDtoTotalSum.get().getCategoryName());
        assertNotNull(optionalCalculateSumDtoTotalSum.get().getSumOfExpense());
    }


    @Test
    public void testAddDates() throws Exception {

        // given
        Optional<LocalDateContainer> optionalLocalDateContainer = calculateSumService.getLocalDateContainer();
        assertTrue(optionalLocalDateContainer.isPresent());

        // when
        calculateSumService.editLocalDateContainer(optionalLocalDateContainer.get().getDateFrom().plusMonths(2), optionalLocalDateContainer.get().getDateTo().minusMonths(3));

        // then
        Optional<LocalDateContainer> updatedOptionalLocalDateContainer = calculateSumService.getLocalDateContainer();
        assertTrue(updatedOptionalLocalDateContainer.isPresent());

        assertEquals(updatedOptionalLocalDateContainer.get().getDateFrom(), optionalLocalDateContainer.get().getDateFrom().plusMonths(2));
        assertEquals(updatedOptionalLocalDateContainer.get().getDateTo(), optionalLocalDateContainer.get().getDateTo().minusMonths(3));

    }


    class MockMvcCalculateSumService {

        public List<CalculateSumDto> findAllWithSumOfExpenses() throws Exception {

            logger.debug("findAllWithSumOfExpenses()");
            MockHttpServletResponse response = mockMvc.perform(get(CALCULATESUM_ENDPOINT)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);


            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<CalculateSumDto>>() {
            });
        }



        public Optional<LocalDateContainer> getLocalDateContainer() throws Exception {

            logger.debug("getLocalDateContainer()");
            MockHttpServletResponse response = mockMvc.perform(get(CALCULATESUM_ENDPOINT + "/dates")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), LocalDateContainer.class));
        }

        public Optional<CalculateSumDto> getTotalSum() throws Exception {

            logger.debug("getTotalSum()");
            MockHttpServletResponse response = mockMvc.perform(get(CALCULATESUM_ENDPOINT + "/totalsum")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), CalculateSumDto.class));
        }

        public void editLocalDateContainer(LocalDate localDateFrom, LocalDate localDateTo) throws Exception {

            logger.debug("editLocalDateContainer with dates({} , {})", localDateFrom, localDateTo);
            LocalDateContainer localDateContainer = new LocalDateContainer(localDateFrom, localDateTo);
            MockHttpServletResponse response =
                    mockMvc.perform(post(CALCULATESUM_ENDPOINT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(localDateContainer))
                                    .accept(MediaType.APPLICATION_JSON)
                            ).andExpect(status().isOk())
                            .andReturn().getResponse();
        }
    }
}
