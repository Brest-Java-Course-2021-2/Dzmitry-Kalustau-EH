package com.epam.brest.model.exceptions;


public class IncorrectExpense extends Exception {

    private Integer categoryId;
    private String exceptionMessage;

    public IncorrectExpense(String message, Integer categoryId) {
        super(message);
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getMessage() {
        return exceptionMessage;
    }

    public String toString() {
        return getClass().getName() + ": " + exceptionMessage + " " + categoryId;
    }
}
