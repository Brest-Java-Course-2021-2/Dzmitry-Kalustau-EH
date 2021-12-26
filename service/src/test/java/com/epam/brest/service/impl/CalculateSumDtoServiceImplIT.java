package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.service.config.ServiceTestConfig;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({ServiceTestConfig.class})
@Transactional
class CalculateSumDtoServiceImplIT {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoServiceImplIT.class);

    @Autowired
    CalculateSumDtoService calculateSumDtoService;

    @Test
    public void testFindAllWithSumOfExpenses() {

        logger.debug("FindAllWithSumOfExpenses");
        List<CalculateSumDto> calculateSumDtoList = calculateSumDtoService.findAllWithSumOfExpenses();
        assertNotNull(calculateSumDtoList);
        assertTrue(calculateSumDtoList.size() > 0);
    }

    @Test
    void testGetTotalSum() {

        logger.debug("Execute IT test: getTotalSum()");
        assertNotNull(calculateSumDtoService);

        CalculateSumDto calculateSumDtoTotalSum = calculateSumDtoService.getTotalSum();
        assertNotNull(calculateSumDtoTotalSum);

        assertEquals("Total Sum", calculateSumDtoTotalSum.getCategoryName());

    }

    @Test
    void testGetLocalDateContainer() {

        logger.debug("Execute IT test: getLocalDateContainer()");
        assertNotNull(calculateSumDtoService);

        LocalDateContainer localDateContainer = calculateSumDtoService.getLocalDateContainer();
        assertNotNull(localDateContainer);
        assertNotNull(localDateContainer.getDateFrom());
        assertNotNull(localDateContainer.getDateTo());

    }

    @Test
    void testEditLocalDateContainer() {

        logger.debug("Execute IT test: editLocalDateContainer()");
        assertNotNull(calculateSumDtoService);

        LocalDateContainer localDateContainerBefore = calculateSumDtoService.getLocalDateContainer();
        assertNotNull(localDateContainerBefore);

        LocalDate localDateFromBefore = localDateContainerBefore.getDateFrom();
        LocalDate localDateToBefore = localDateContainerBefore.getDateTo();
        assertNotNull(localDateFromBefore);
        assertNotNull(localDateToBefore);

        LocalDateContainer localDateContainerAfter = new LocalDateContainer(localDateFromBefore.minusMonths(1), localDateToBefore.plusMonths(2));
        calculateSumDtoService.editLocalDateContainer(localDateContainerAfter.getDateFrom(), localDateContainerAfter.getDateTo());

        LocalDateContainer updatedLocalDateContainer = calculateSumDtoService.getLocalDateContainer();
        assertNotNull(updatedLocalDateContainer);
        assertNotNull(updatedLocalDateContainer.getDateFrom());
        assertNotNull(updatedLocalDateContainer.getDateTo());
        assertEquals(localDateFromBefore.minusMonths(1), updatedLocalDateContainer.getDateFrom());
        assertEquals(localDateToBefore.plusMonths(2), updatedLocalDateContainer.getDateTo());

    }

}
