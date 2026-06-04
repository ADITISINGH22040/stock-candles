package com.stockcandles.service;

import com.stockcandles.dto.StockCandleAggregationResponse;
import com.stockcandles.enums.CandleInterval;
import com.stockcandles.model.StockCandle;
import com.stockcandles.model.StockCandleKey;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockCandleAggregationHelperTest {

    @Test
    void aggregateCandles_toFifteenMinuteBuckets() {
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        List<StockCandle> rawCandles = List.of(
                createCandle("RELIANCE", LocalDateTime.of(2024, 1, 15, 9, 15, 0), zone, "2450.50", "2452.00", "2448.00", "2451.00", 100L),
                createCandle("RELIANCE", LocalDateTime.of(2024, 1, 15, 9, 16, 0), zone, "2451.00", "2455.00", "2450.00", "2453.00", 200L),
                createCandle("RELIANCE", LocalDateTime.of(2024, 1, 15, 9, 30, 0), zone, "2453.00", "2457.00", "2452.00", "2456.00", 150L)
        );

        List<StockCandleAggregationResponse.Candle> result = StockCandleAggregationHelper.aggregate(rawCandles, CandleInterval.FIFTEEN_MINUTES, zone);

        assertEquals(2, result.size());
        assertEquals(Instant.parse("2024-01-15T03:45:00Z"), result.get(0).getDatetime());
        assertEquals(new BigDecimal("2450.50"), result.get(0).getOpen());
        assertEquals(new BigDecimal("2455.00"), result.get(0).getHigh());
        assertEquals(new BigDecimal("2448.00"), result.get(0).getLow());
        assertEquals(new BigDecimal("2453.00"), result.get(0).getClose());
        assertEquals(300L, result.get(0).getVolume());

        assertEquals(Instant.parse("2024-01-15T04:00:00Z"), result.get(1).getDatetime());
        assertEquals(new BigDecimal("2453.00"), result.get(1).getOpen());
        assertEquals(new BigDecimal("2457.00"), result.get(1).getHigh());
        assertEquals(new BigDecimal("2452.00"), result.get(1).getLow());
        assertEquals(new BigDecimal("2456.00"), result.get(1).getClose());
        assertEquals(150L, result.get(1).getVolume());
    }

    private StockCandle createCandle(String symbol,
                                     LocalDateTime localDateTime,
                                     ZoneId zone,
                                     String open,
                                     String high,
                                     String low,
                                     String close,
                                     long volume) {
        StockCandle candle = new StockCandle();
        StockCandleKey key = new StockCandleKey();
        key.setSymbol(symbol);
        key.setTradingDate(localDateTime.toLocalDate());
        key.setCandleTime(localDateTime.atZone(zone).toInstant());
        candle.setKey(key);
        candle.setOpen(new BigDecimal(open));
        candle.setHigh(new BigDecimal(high));
        candle.setLow(new BigDecimal(low));
        candle.setClose(new BigDecimal(close));
        candle.setVolume(volume);
        return candle;
    }
}
