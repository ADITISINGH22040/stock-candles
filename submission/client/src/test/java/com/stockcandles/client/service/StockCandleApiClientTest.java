package com.stockcandles.client.service;

import com.stockcandles.client.config.ClientProperties;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StockCandleApiClientTest {

    @Test
    void buildCandlesUrl_containsQueryParameters() {
        StockCandleApiClient client = new StockCandleApiClient(
                new ClientProperties("http://localhost:8080"));

        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 15, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 15, 30, 0);

        String url = client.buildCandlesUrl("AAPL", "15m", start, end, 1, 150);

        assertTrue(url.contains("symbol=AAPL"));
        assertTrue(url.contains("timeframe=15m"));
        assertTrue(url.contains("start_date=2024-01-15+09%3A15%3A00"));
        assertTrue(url.contains("end_date=2024-01-15+15%3A30%3A00"));
        assertTrue(url.contains("page=1"));
        assertTrue(url.contains("size=150"));
    }
}
