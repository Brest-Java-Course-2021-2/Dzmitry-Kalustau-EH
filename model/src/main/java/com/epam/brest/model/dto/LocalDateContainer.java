package com.epam.brest.model.dto;

import java.time.LocalDate;

public class LocalDateContainer {

    private String localDateFrom, localDateTo;
    private LocalDate dateFrom, dateTo;

    public LocalDateContainer() {
    }

    public LocalDateContainer(String localDateFrom, String localDateTo) {
        this.localDateFrom = localDateFrom;
        this.localDateTo = localDateTo;
    }

    public String getLocalDateFrom() {
        return localDateFrom;
    }

    public void setLocalDateFrom(String localDateFrom) {
        this.localDateFrom = localDateFrom;
    }

    public String getLocalDateTo() {
        return localDateTo;
    }

    public void setLocalDateTo(String localDateTo) {
        this.localDateTo = localDateTo;
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


    public LocalDate parseDateFrom() {
        return LocalDate.parse(localDateFrom);
    }

    public LocalDate parseDateTo() {
        return LocalDate.parse(localDateTo);
    }

}