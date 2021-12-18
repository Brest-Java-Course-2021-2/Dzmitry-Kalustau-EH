package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.service.config.ServiceTestConfig;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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

}
