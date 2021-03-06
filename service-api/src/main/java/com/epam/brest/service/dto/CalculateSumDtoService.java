package com.epam.brest.service.dto;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;
import com.epam.brest.model.dto.ReportDto;

import java.time.LocalDate;
import java.util.List;

/**
 * CalculateSumDto Service Interface.
 */
public interface CalculateSumDtoService {

    /**
     * Get all categories with total expenses
     * @return CalculateSumDto list
     */
    List<CalculateSumDto> findAllWithSumOfExpenses();

    /**
     * Get LocalDate Container with current dates
     * @return LocalDate Container
     */
    LocalDateContainer getLocalDateContainer();

    /**
     * Changes dates in LocalDate container
     * @param localDateFrom - date from
     * @param localDateTo - date to
     */
    void editLocalDateContainer(LocalDate localDateFrom, LocalDate localDateTo);

    /**
     * Get CalculateSum Dto with Total Sum
     * @return CalculateSumDto object with Total SUm
     */
    CalculateSumDto getTotalSum();

    /**
     * Create report in MongoDB
     */
    ReportDto createReport(Integer months);
}
