package com.stockcandles.client.cli;

import com.stockcandles.client.dto.StockCandleAggregationResponse;
import com.stockcandles.client.dto.StockCandleItem;
import com.stockcandles.client.service.StockCandleApiClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CandleQueryCommand {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StockCandleApiClient apiClient;

    public CandleQueryCommand(StockCandleApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void run(String[] args) {
        if (args.length < 4) {
            printUsage();
            return;
        }

        String symbol = args[0];
        String timeframe = args[1];
        LocalDateTime startDate = parseDateTime(args[2]);
        LocalDateTime endDate = parseDateTime(args[3]);
        Integer page = args.length > 4 ? Integer.parseInt(args[4]) : 0;
        Integer size = args.length > 5 ? Integer.parseInt(args[5]) : 200;

        StockCandleAggregationResponse response = apiClient.fetchCandles(symbol, timeframe, startDate, endDate, page, size);
        printResponse(response);
    }

    private LocalDateTime parseDateTime(String text) {
        try {
            return LocalDateTime.parse(text, DATE_TIME_FORMATTER);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Date must be in yyyy-MM-dd HH:mm:ss format");
        }
    }

    private void printResponse(StockCandleAggregationResponse response) {
        System.out.println("=== Fetched Candle Data ===");
        System.out.printf("Symbol: %s | Timeframe: %s | Page: %s | Page Size: %s | Candles Returned: %d%n",
                response.getSymbol(),
                response.getTimeframe(),
                response.getPage() != null ? response.getPage() : 0,
                response.getPageSize() != null ? response.getPageSize() : 200,
                response.getCount());

        List<StockCandleItem> candles = response.getCandles();
        if (candles != null) {
            for (int i = 0; i < candles.size(); i++) {
                StockCandleItem candle = candles.get(i);
                System.out.printf("%d %s | O: %s | H: %s | L: %s | C: %s | V: %s%n",
                        i + 1,
                        candle.getDatetime(),
                        candle.getOpen(),
                        candle.getHigh(),
                        candle.getLow(),
                        candle.getClose(),
                        candle.getVolume());
            }
        }
        System.out.println("===========================");
    }

    private void printUsage() {
        System.out.println("Usage: java -jar stock-candles-client.jar <symbol> <timeframe> <start_date> <end_date>");
        System.out.println("Example: java -jar stock-candles-client.jar RELIANCE 15m \"2024-01-15 09:15:00\" \"2024-01-15 15:30:00\"");
    }
}
