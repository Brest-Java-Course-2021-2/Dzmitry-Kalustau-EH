package com.epam.brest.service.impl.dto;

import com.epam.brest.dao.dto.CalculateSumDtoDao;
import com.epam.brest.dao.report.CustomReportRepository;
import com.epam.brest.dao.report.ReportRepository;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.model.dto.ReportDto;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class CalculateSumDtoServiceImpl implements CalculateSumDtoService {

    private final CalculateSumDtoDao calculateSumDtoDao;

    private final ReportRepository reportRepository;

    @Autowired
    public CalculateSumDtoServiceImpl(CalculateSumDtoDao calculateSumDtoDao, ReportRepository reportRepository) {
        this.calculateSumDtoDao = calculateSumDtoDao;
        this.reportRepository = reportRepository;
    }


    @Override
    public List<CalculateSumDto> findAllWithSumOfExpenses() {
        return calculateSumDtoDao.findAllWithSumOfExpenses();
    }

    @Override
    public LocalDateContainer getLocalDateContainer() {
        return calculateSumDtoDao.getLocalDateContainer();
    }

    @Override
    public void editLocalDateContainer(LocalDate localDateFrom, LocalDate localDateTo) {
        calculateSumDtoDao.editLocalDateContainer(localDateFrom, localDateTo);
    }

    @Override
    public CalculateSumDto getTotalSum() {
        return calculateSumDtoDao.getTotalSum();
    }

    @Override
    public ReportDto createReport(Integer months) {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusMonths(months);

        List<CalculateSumDto> dtoList = calculateSumDtoDao.findSumOfExpensesByDates(previousDate, currentDate);
        ReportDto reportDto = createReportDto(previousDate, currentDate, dtoList);

        return reportRepository.save(reportDto);
//        return customReportRepository.saveOrUpdate(reportDto);
    }

    private ReportDto createReportDto(LocalDate dateFrom, LocalDate dateTo, List<CalculateSumDto> dtoList) {

        BigDecimal totalSum = createTotalSum(dtoList);
        ReportDto reportDto = new ReportDto(dateFrom, dateTo, dtoList, totalSum);
        reportDto.setId("6291e05599f911029f33c695");

        return reportDto;
    }


    private BigDecimal createTotalSum(List<CalculateSumDto> dtoList) {
        if (dtoList == null) {
            return new BigDecimal("0");
        }


        BigDecimal finalSumOfExpenses = dtoList.stream().map((s) -> s.getSumOfExpense()).
                                                         reduce((s1, s2) -> s1.add(s2)).
                                                         orElse(new BigDecimal(0));


        return finalSumOfExpenses;
    }

}