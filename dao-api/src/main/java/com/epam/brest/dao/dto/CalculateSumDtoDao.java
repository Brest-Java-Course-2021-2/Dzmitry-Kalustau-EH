package com.epam.brest.dao.dto;

import com.epam.brest.model.dto.CalculateSumDto;
import com.epam.brest.model.dto.LocalDateContainer;

import java.time.LocalDate;
import java.util.List;

/**
 * CalculateSumDto DAO Interface.
 */
public interface CalculateSumDtoDao {

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
}
