package com.stockcandles.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class StockCandleAggregationResponse {

    private String symbol;
    private String timeframe;
    private List<Candle> candles;
    private int count;
    private Integer page;
    private Integer pageSize;

    public StockCandleAggregationResponse() {
    }

    public StockCandleAggregationResponse(String symbol, String timeframe, List<Candle> candles, int count) {
        this(symbol, timeframe, candles, count, null, null);
    }

    public StockCandleAggregationResponse(String symbol, String timeframe, List<Candle> candles, int count, Integer page, Integer pageSize) {
        this.symbol = symbol;
        this.timeframe = timeframe;
        this.candles = candles;
        this.count = count;
        this.page = page;
        this.pageSize = pageSize;
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

    public List<Candle> getCandles() {
        return candles;
    }

    public void setCandles(List<Candle> candles) {
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

    public static class Candle {

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Instant datetime;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private Long volume;

        public Candle() {
        }

        public Candle(Instant datetime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Long volume) {
            this.datetime = datetime;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }

        public Instant getDatetime() {
            return datetime;
        }

        public void setDatetime(Instant datetime) {
            this.datetime = datetime;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }
    }
}
