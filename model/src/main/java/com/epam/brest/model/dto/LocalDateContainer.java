package com.epam.brest.model.dto;

import java.time.LocalDate;

public class LocalDateContainer {

    private LocalDate dateFrom, dateTo;

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