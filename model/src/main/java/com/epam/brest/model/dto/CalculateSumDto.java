package com.epam.brest.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class CalculateSumDto {

    @ApiModelProperty(notes = "Category name", example = "Food")
    private String categoryName;

    @ApiModelProperty(notes = "Sum of expense", example = "15.5")
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

    @Override
    public String toString() {
        return "CalculateSumDto{" +
                "categoryName='" + categoryName + '\'' +
                ", sumOfExpense=" + sumOfExpense +
                '}';
    }
}

