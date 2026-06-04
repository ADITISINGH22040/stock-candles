package com.stockcandles.dto;

public class StockCandleQueryRequest {

    private String symbol;
    private String timeframe;
    private String startDate;
    private String endDate;
    private Integer page;
    private Integer size;

    public StockCandleQueryRequest() {
    }

    public StockCandleQueryRequest(String symbol, String timeframe, String startDate, String endDate) {
        this(symbol, timeframe, startDate, endDate, 0, 200);
    }

    public StockCandleQueryRequest(String symbol, String timeframe, String startDate, String endDate, Integer page, Integer size) {
        this.symbol = symbol;
        this.timeframe = timeframe;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
