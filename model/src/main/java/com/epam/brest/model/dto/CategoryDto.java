package com.epam.brest.model.dto;

import java.math.BigDecimal;

public class CategoryDto {

    private String categoryName;

    private BigDecimal sumOfExpense;

    public CategoryDto() {
    }

    public CategoryDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryDto(String categoryName, BigDecimal sumOfExpense) {
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

