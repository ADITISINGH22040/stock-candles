package com.stockcandles.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@PrimaryKeyClass
public class StockCandleKey implements Serializable {

    @PrimaryKeyColumn(name = "symbol", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String symbol;

    @PrimaryKeyColumn(name = "trading_date", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private LocalDate tradingDate;

    @PrimaryKeyColumn(name = "candle_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private Instant candleTime;

    public StockCandleKey() {
    }

    public StockCandleKey(String symbol, LocalDate tradingDate, Instant candleTime) {
        this.symbol = symbol;
        this.tradingDate = tradingDate;
        this.candleTime = candleTime;
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

    public Instant getCandleTime() {
        return candleTime;
    }

    public void setCandleTime(Instant candleTime) {
        this.candleTime = candleTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockCandleKey that = (StockCandleKey) o;
        return Objects.equals(symbol, that.symbol)
                && Objects.equals(tradingDate, that.tradingDate)
                && Objects.equals(candleTime, that.candleTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, tradingDate, candleTime);
    }
}
