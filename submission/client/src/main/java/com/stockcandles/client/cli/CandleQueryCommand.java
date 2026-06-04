package com.stockcandles.client.cli;

import com.stockcandles.client.dto.StockCandleResponse;
import com.stockcandles.client.service.StockCandleApiClient;

import java.time.LocalDate;
import java.util.List;

public class CandleQueryCommand {

    private final StockCandleApiClient apiClient;

    public CandleQueryCommand(StockCandleApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void run(String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }

        String symbol = args[0];
        LocalDate tradingDate = LocalDate.parse(args[1]);
        List<StockCandleResponse> candles = apiClient.fetchCandles(symbol, tradingDate);
        System.out.println("Fetched " + candles.size() + " candle(s) for " + symbol + " on " + tradingDate);
    }

    private void printUsage() {
        System.out.println("Usage: java -jar stock-candles-client.jar <symbol> <trading-date>");
        System.out.println("Example: java -jar stock-candles-client.jar AAPL 2024-01-15");
    }
}
