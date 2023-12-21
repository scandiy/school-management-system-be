package com.students2.student2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class EndOfDayId implements Serializable {
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    public EndOfDayId(String symbol, Date date) {
        this.symbol = symbol;
        this.date = date;
    }

    public EndOfDayId() {

    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
