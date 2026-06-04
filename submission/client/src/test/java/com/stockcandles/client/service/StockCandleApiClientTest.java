package com.stockcandles.client.service;

import com.stockcandles.client.config.ClientProperties;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StockCandleApiClientTest {

    @Test
    void buildCandlesUrl_containsQueryParameters() {
        StockCandleApiClient client = new StockCandleApiClient(
                new ClientProperties("http://localhost:8080"));

        String url = client.buildCandlesUrl("AAPL", LocalDate.of(2024, 1, 15));

        assertTrue(url.contains("symbol=AAPL"));
        assertTrue(url.contains("tradingDate=2024-01-15"));
    }
}
