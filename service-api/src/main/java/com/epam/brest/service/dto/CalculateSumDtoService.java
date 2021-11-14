package com.epam.brest.service.dto;

import com.epam.brest.model.dto.CalculateSumDto;

import java.util.List;

public interface CalculateSumDtoService {

    List<CalculateSumDto> findAllWithSumOfExpenses();

}
