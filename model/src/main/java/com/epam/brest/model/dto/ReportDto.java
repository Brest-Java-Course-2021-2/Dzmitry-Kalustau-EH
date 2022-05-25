package com.epam.brest.model.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "expenses")
public class ReportDto {

    @Id
    private String id;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private List<CalculateSumDto> expensesList;

    private String totalName;
    private BigDecimal totalExpense;

    public ReportDto() {
        this.totalName = "Total Sum";
        this.totalExpense = new BigDecimal("0");
    }

    public ReportDto(LocalDate dateFrom, LocalDate dateTo, List<CalculateSumDto> expensesList, BigDecimal totalExpense) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.expensesList = expensesList;
        this.totalName = "Total Sum";
        this.totalExpense = totalExpense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public List<CalculateSumDto> getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(List<CalculateSumDto> expensesList) {
        this.expensesList = expensesList;
    }

    public String getTotalName() {
        return totalName;
    }

    public void setTotalName(String totalName) {
        this.totalName = totalName;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }
}
