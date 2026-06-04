package com.stockcandles.service;

import com.stockcandles.dto.StockCandleAggregationResponse;
import com.stockcandles.dto.StockCandleQueryRequest;
import com.stockcandles.model.StockCandle;
import com.stockcandles.model.StockCandleKey;
import org.junit.jupiter.api.Test;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StockCandleServiceImplTest {

    @Test
    void findCandles_appliesPaginationAndReturnsExpectedPage() {
        CassandraTemplate cassandraTemplate = mock(CassandraTemplate.class);
        List<StockCandle> rawCandles = new ArrayList<>();
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        LocalDate tradingDate = LocalDate.of(2024, 1, 15);

        for (int minute = 0; minute < 5; minute++) {
            StockCandle candle = new StockCandle();
            candle.setKey(new StockCandleKey(
                    "AAPL",
                    tradingDate,
                    tradingDate.atTime(9, 15 + minute).atZone(zone).toInstant()));
            candle.setOpen(BigDecimal.valueOf(100 + minute));
            candle.setHigh(BigDecimal.valueOf(101 + minute));
            candle.setLow(BigDecimal.valueOf(99 + minute));
            candle.setClose(BigDecimal.valueOf(100 + minute));
            candle.setVolume(100L + minute);
            rawCandles.add(candle);
        }

        when(cassandraTemplate.select(any(Query.class), any(Class.class))).thenReturn(rawCandles);
        StockCandleServiceImpl service = new StockCandleServiceImpl(cassandraTemplate);

        StockCandleQueryRequest request = new StockCandleQueryRequest(
                "AAPL",
                "1m",
                "2024-01-15 09:15:00",
                "2024-01-15 09:19:00",
                1,
                2);

        StockCandleAggregationResponse response = service.findCandles(request);

        assertEquals(2, response.getCount());
        assertEquals(1, response.getPage());
        assertEquals(2, response.getPageSize());
        assertEquals("AAPL", response.getSymbol());
        assertEquals("1m", response.getTimeframe());
        assertEquals(2, response.getCandles().size());
        assertEquals(102.0, response.getCandles().get(0).getOpen().doubleValue());
    }
}
