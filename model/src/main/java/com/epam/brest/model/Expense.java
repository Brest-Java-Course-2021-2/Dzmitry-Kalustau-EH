package com.epam.brest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {

    private Integer expenseId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate dateOfExpense;

    private Integer categoryId;

    private BigDecimal sumOfExpense;

    public Expense() {
    }

    public Expense(Integer expenseId) {
        this.expenseId = expenseId;
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
}
