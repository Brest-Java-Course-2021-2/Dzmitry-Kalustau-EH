package com.epam.brest.service.impl;

import com.epam.brest.dao.dto.CalculateSumDtoDao;
import com.epam.brest.dao.report.ReportRepository;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.ReportDto;
import com.epam.brest.service.impl.dto.CalculateSumDtoServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
public class CalculateSumDtoServiceImplTest {

    private final Logger logger = LogManager.getLogger(CalculateSumDtoServiceImplTest.class);

    @InjectMocks
    CalculateSumDtoServiceImpl calculateSumDtoService;

    @Mock
    ReportRepository reportRepository;

    @Mock
    CalculateSumDtoDao calculateSumDtoDao;


    @Test
    void testCreateReport() {

        logger.debug("Execute test createReport()");

        ReportDto reportDto = getReportDto();
        Integer monthCount = 3;

        Mockito.when(reportRepository.save(any())).thenReturn(reportDto);
        ReportDto report = calculateSumDtoService.createReport(monthCount);

        Mockito.verify(reportRepository).save(any());


        Assertions.assertNotNull(report);
        Assertions.assertSame(reportDto.getTotalExpense(), report.getTotalExpense());
    }


    @Test
    void testFindAllWithSumOfExpenses() {

        CalculateSumDto calculateSumDto = new CalculateSumDto();
        List<CalculateSumDto> list = Collections.singletonList(calculateSumDto);

        Mockito.when(calculateSumDtoDao.findAllWithSumOfExpenses()).thenReturn(list);
        List<CalculateSumDto> result = calculateSumDtoService.findAllWithSumOfExpenses();
        Mockito.verify(calculateSumDtoDao).findAllWithSumOfExpenses();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertSame(calculateSumDto, result.get(0));

    }

    private ReportDto getReportDto() {
        ReportDto reportDto = new ReportDto();
        CalculateSumDto calculateSumDto = new CalculateSumDto();

        reportDto.setId("6291e05599f911029f33c695");
        reportDto.setDateFrom(LocalDate.of(2022, 4, 5));
        reportDto.setDateTo(LocalDate.of(2022, 7, 5));
        reportDto.setExpensesList(Collections.singletonList(calculateSumDto));
        reportDto.setTotalName("Test total name");
        reportDto.setTotalExpense(new BigDecimal("2"));

        return reportDto;
    }
}
