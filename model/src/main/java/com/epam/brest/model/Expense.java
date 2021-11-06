package com.epam.brest.model;

import java.util.Date;

public class Expense {

    private Integer expenseId;

    private Date dateOfExpense;

    private Integer categoryId;

    private Double sumOfExpense;

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Date getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(Date dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getSumOfExpense() {
        return sumOfExpense;
    }

    public void setSumOfExpense(Double sumOfExpense) {
        this.sumOfExpense = sumOfExpense;
    }
}
