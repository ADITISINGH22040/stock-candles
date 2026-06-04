package com.stockcandles.client.dto;

import java.util.List;

public class StockCandleAggregationResponse {

    private String symbol;
    private String timeframe;
    private List<StockCandleItem> candles;
    private int count;
    private Integer page;
    private Integer pageSize;

    public StockCandleAggregationResponse() {
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

    public List<StockCandleItem> getCandles() {
        return candles;
    }

    public void setCandles(List<StockCandleItem> candles) {
        this.candles = candles;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
