package com.epam.brest.model;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Expense {

    @ApiModelProperty(notes = "Expense id", example = "1")
    private Integer expenseId;

    @ApiModelProperty(notes = "Date of expense")
    private LocalDate dateOfExpense;

    @ApiModelProperty(notes = "Category id", example = "1")
    private Integer categoryId;

    @ApiModelProperty(notes = "Sum of expense", example = "10.2")
    private BigDecimal sumOfExpense;

    public Expense() {
    }

    public Expense(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Expense(LocalDate dateOfExpense, Integer categoryId) {
        this.dateOfExpense = dateOfExpense;
        this.categoryId = categoryId;
    }

    public Expense(BigDecimal sumOfExpense) {
        this.sumOfExpense = sumOfExpense;
    }

    public Expense(Integer expenseId, LocalDate dateOfExpense) {
        this.expenseId = expenseId;
        this.dateOfExpense = dateOfExpense;
    }

    public Expense(LocalDate dateOfExpense, Integer categoryId, BigDecimal sumOfExpense) {
        this.dateOfExpense = dateOfExpense;
        this.categoryId = categoryId;
        this.sumOfExpense = sumOfExpense;
    }

    public Expense(Integer expenseId, LocalDate dateOfExpense, Integer categoryId, BigDecimal sumOfExpense) {
        this.expenseId = expenseId;
        this.dateOfExpense = dateOfExpense;
        this.categoryId = categoryId;
        this.sumOfExpense = sumOfExpense;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public LocalDate getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(LocalDate dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getSumOfExpense() {
        return sumOfExpense;
    }

    public void setSumOfExpense(BigDecimal sumOfExpense) {
        this.sumOfExpense = sumOfExpense;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", dateOfExpense='" + dateOfExpense + '\'' +
                ", categoryId=" + categoryId +
                ", sumOfExpense=" + sumOfExpense +
                '}';
    }
}
