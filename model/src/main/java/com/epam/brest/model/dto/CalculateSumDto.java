package com.epam.brest.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CalculateSumDto {

    private String categoryName;

    private BigDecimal sumOfExpense;

    public CalculateSumDto() {
    }

    public CalculateSumDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public CalculateSumDto(String categoryName, BigDecimal sumOfExpense) {
        this.categoryName = categoryName;
        this.sumOfExpense = sumOfExpense;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getSumOfExpense() {
        return sumOfExpense;
    }

    public void setSumOfExpense(BigDecimal sumOfExpense) {
        this.sumOfExpense = sumOfExpense;
    }

}

