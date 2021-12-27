package com.epam.brest.rest.exception;

import com.epam.brest.model.exceptions.IncorrectExpense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    public static final String INCORRECT_EXPENSE = "expense.category_not_found";
    public static final String VALIDATION_ERROR = "validation_error";

    @ExceptionHandler(com.epam.brest.model.exceptions.IncorrectExpense.class)
    public final ResponseEntity<ErrorResponse> handleExpenseWithIncorrectCategory(IncorrectExpense ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(INCORRECT_EXPENSE, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception ex, WebRequest request) {

        return new ResponseEntity<>(
                new ErrorResponse(VALIDATION_ERROR, ex),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
