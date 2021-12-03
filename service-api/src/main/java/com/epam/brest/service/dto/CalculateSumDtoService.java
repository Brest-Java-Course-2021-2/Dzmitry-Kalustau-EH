package com.epam.brest.service.dto;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;

import java.time.LocalDate;
import java.util.List;

public interface CalculateSumDtoService {

    List<CalculateSumDto> findAllWithSumOfExpenses();

    List<CalculateSumDto> findSumOfExpensesBetweenDates(String localDateFrom, String localDateTo);

    LocalDateContainer getLocalDateContainer();

    void editLocalDateContainer(String localDateFrom, String localDateTo);

}
