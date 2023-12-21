package com.students2.student2.services.marketstack.dto;

import java.util.Date;

public class EndOfDayDTO {
    private String symbol;

    private Date date;

    private float close;

    public EndOfDayDTO(String symbol, Date date, float close) {
        this.symbol = symbol;
        this.date = date;
        this.close = close;
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

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }
}
