package com.epam.brest.service.impl.dto;

import com.epam.brest.dao.dto.CalculateSumDtoDao;
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

    private ReportRepository reportRepository;

    @Autowired
    public CalculateSumDtoServiceImpl(CalculateSumDtoDao calculateSumDtoDao) {
        this.calculateSumDtoDao = calculateSumDtoDao;
    }

    @Autowired
    public void setReportRepository(ReportRepository reportRepository) {
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
    public void createReport(Integer months) {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusMonths(months);

        List<CalculateSumDto> dtoList = calculateSumDtoDao.findSumOfExpensesByDates(previousDate, currentDate);
        ReportDto reportDto = createReportDto(previousDate, currentDate, dtoList);

        reportRepository.save(reportDto);


    }

    private ReportDto createReportDto(LocalDate dateFrom, LocalDate dateTo, List<CalculateSumDto> dtoList) {

        BigDecimal totalSum = createTotalSum(dtoList);
        return new ReportDto(dateFrom, dateTo, dtoList, totalSum);
    }


    private BigDecimal createTotalSum(List<CalculateSumDto> dtoList) {
        if (dtoList == null) {
            return new BigDecimal("0");
        }

        BigDecimal finalSumOfExpenses = new BigDecimal(0);
        for (CalculateSumDto sum : dtoList) {
            finalSumOfExpenses = finalSumOfExpenses.add(sum.getSumOfExpense());
        }
        return finalSumOfExpenses;
    }

}