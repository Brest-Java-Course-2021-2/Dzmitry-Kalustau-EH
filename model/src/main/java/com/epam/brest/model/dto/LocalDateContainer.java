package com.epam.brest.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

public class LocalDateContainer {

    @ApiModelProperty(notes = "Date from", example = "2021-09-10")
    private LocalDate dateFrom;

    @ApiModelProperty(notes = "Date to", example = "2021-10-12")
    private LocalDate dateTo;

    public LocalDateContainer() {
    }

    public LocalDateContainer(LocalDate dateFrom, LocalDate dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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


}