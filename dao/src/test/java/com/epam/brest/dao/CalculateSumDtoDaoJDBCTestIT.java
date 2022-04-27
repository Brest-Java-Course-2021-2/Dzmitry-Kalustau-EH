package com.epam.brest.dao;

import com.epam.brest.dao.dto.CalculateSumDtoDao;
import com.epam.brest.dao.dto.CalculateSumDtoDaoJdbc;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.testdb.SpringTestJdbcConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({CalculateSumDtoDaoJdbc.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringTestJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
class CalculateSumDtoDaoJDBCTestIT {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoDaoJDBCTestIT.class);

    private CalculateSumDtoDaoJdbc calculateSumDtoDaoJdbc;

    public CalculateSumDtoDaoJDBCTestIT(@Autowired CalculateSumDtoDao calculateSumDtoDaoJdbc) {
        this.calculateSumDtoDaoJdbc = (CalculateSumDtoDaoJdbc) calculateSumDtoDaoJdbc;
    }

    @Test
    void testFindAllWithSumOfExpenses() {

        logger.debug("Execute IT test: findAllWithSumOfExpenses()");
        assertNotNull(calculateSumDtoDaoJdbc);
        assertNotNull(calculateSumDtoDaoJdbc.findAllWithSumOfExpenses());
    }

    @Test
    void testGetTotalSum() {

        logger.debug("Execute IT test: getTotalSum()");
        assertNotNull(calculateSumDtoDaoJdbc);

        CalculateSumDto calculateSumDtoTotalSum = calculateSumDtoDaoJdbc.getTotalSum();
        assertNotNull(calculateSumDtoTotalSum);

        assertEquals("Total Sum", calculateSumDtoTotalSum.getCategoryName());

    }

    @Test
    void testGetLocalDateContainer() {

        logger.debug("Execute IT test: getLocalDateContainer()");
        assertNotNull(calculateSumDtoDaoJdbc);

        LocalDateContainer localDateContainer = calculateSumDtoDaoJdbc.getLocalDateContainer();
        assertNotNull(localDateContainer);
        assertNotNull(localDateContainer.getDateFrom());
        assertNotNull(localDateContainer.getDateTo());

    }

    @Test
    void testEditLocalDateContainer() {

        logger.debug("Execute IT test: editLocalDateContainer()");
        assertNotNull(calculateSumDtoDaoJdbc);

        LocalDateContainer localDateContainerBefore = calculateSumDtoDaoJdbc.getLocalDateContainer();
        assertNotNull(localDateContainerBefore);

        LocalDate localDateFromBefore = localDateContainerBefore.getDateFrom();
        LocalDate localDateToBefore = localDateContainerBefore.getDateTo();
        assertNotNull(localDateFromBefore);
        assertNotNull(localDateToBefore);

        LocalDateContainer localDateContainerAfter = new LocalDateContainer(localDateFromBefore.minusMonths(1), localDateToBefore.plusMonths(2));
        calculateSumDtoDaoJdbc.editLocalDateContainer(localDateContainerAfter.getDateFrom(), localDateContainerAfter.getDateTo());

        LocalDateContainer updatedLocalDateContainer = calculateSumDtoDaoJdbc.getLocalDateContainer();
        assertNotNull(updatedLocalDateContainer);
        assertNotNull(updatedLocalDateContainer.getDateFrom());
        assertNotNull(updatedLocalDateContainer.getDateTo());
        assertEquals(localDateFromBefore.minusMonths(1), updatedLocalDateContainer.getDateFrom());
        assertEquals(localDateToBefore.plusMonths(2), updatedLocalDateContainer.getDateTo());

    }

}