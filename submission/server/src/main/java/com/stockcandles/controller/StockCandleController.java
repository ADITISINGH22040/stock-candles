package com.stockcandles.controller;

import com.stockcandles.dto.StockCandleAggregationResponse;
import com.stockcandles.dto.StockCandleQueryRequest;
import com.stockcandles.service.StockCandleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/candles")
@Tag(name = "Stock Candles", description = "Stock candle data aggregation endpoints")
public class StockCandleController {

    private final StockCandleService stockCandleService;

    public StockCandleController(StockCandleService stockCandleService) {
        this.stockCandleService = stockCandleService;
    }

    @GetMapping
    @Operation(summary = "Query stock candles by symbol, timeframe, and date range")
    public ResponseEntity<StockCandleAggregationResponse> getCandles(
            @RequestParam String symbol,
            @RequestParam String timeframe,
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size) {
        StockCandleQueryRequest request = new StockCandleQueryRequest(symbol, timeframe, startDate, endDate, page, size);
        StockCandleAggregationResponse response = stockCandleService.findCandles(request);
        return ResponseEntity.ok(response);
    }
}
