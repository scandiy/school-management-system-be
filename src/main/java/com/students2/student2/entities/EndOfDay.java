package com.students2.student2.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@IdClass(EndOfDayId.class)
@Table(name = "endofday", schema = "school")
public class EndOfDay {

    @Id
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "close", nullable = false)
    private float close;

    public EndOfDay() {
    }

    public EndOfDay(String symbol, float close, Date date) {
        this.symbol = symbol;
        this.close = close;
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
