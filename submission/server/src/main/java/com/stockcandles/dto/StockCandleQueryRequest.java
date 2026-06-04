package com.stockcandles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class StockCandleQueryRequest {

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotNull(message = "Trading date is required")
    private LocalDate tradingDate;

    public StockCandleQueryRequest() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }
}
