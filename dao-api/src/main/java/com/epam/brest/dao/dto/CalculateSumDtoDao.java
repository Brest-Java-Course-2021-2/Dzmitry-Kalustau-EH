package com.epam.brest.dao.dto;

import com.epam.brest.model.dto.CalculateSumDto;

import java.util.List;

public interface CalculateSumDtoDao {

    List<CalculateSumDto> findAllWithSumOfExpenses();

}
