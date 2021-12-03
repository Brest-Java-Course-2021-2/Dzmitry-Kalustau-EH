package com.epam.brest.service.impl.dto;

import com.epam.brest.dao.dto.CalculateSumDtoDao;
import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.service.dto.CalculateSumDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class CalculateSumDtoServiceImpl implements CalculateSumDtoService {

    private final CalculateSumDtoDao calculateSumDtoDao;

    public CalculateSumDtoServiceImpl(CalculateSumDtoDao calculateSumDtoDao) {
        this.calculateSumDtoDao = calculateSumDtoDao;
    }

    @Override
    public List<CalculateSumDto> findAllWithSumOfExpenses() {
        return calculateSumDtoDao.findAllWithSumOfExpenses();
    }

    public List<CalculateSumDto> findSumOfExpensesBetweenDates(String localDateFrom, String localDateTo) {
        return calculateSumDtoDao.findSumOfExpensesBetweenDates(localDateFrom, localDateTo);
    }


    @Override
    public LocalDateContainer getLocalDateContainer() {
        return calculateSumDtoDao.getLocalDateContainer();
    }

    @Override
    public void editLocalDateContainer(String localDateFrom, String localDateTo) {
        calculateSumDtoDao.editLocalDateContainer(localDateFrom, localDateTo);
    }
}
