package com.stockcandles.controller;

import com.stockcandles.dto.StockCandleQueryRequest;
import com.stockcandles.dto.StockCandleResponse;
import com.stockcandles.service.StockCandleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candles")
@Tag(name = "Stock Candles", description = "Stock candle data aggregation endpoints")
public class StockCandleController {

    private final StockCandleService stockCandleService;

    public StockCandleController(StockCandleService stockCandleService) {
        this.stockCandleService = stockCandleService;
    }

    @GetMapping
    @Operation(summary = "Query stock candles by symbol and trading date")
    public ResponseEntity<List<StockCandleResponse>> getCandles(
            @Valid @ModelAttribute StockCandleQueryRequest request) {
        List<StockCandleResponse> candles = stockCandleService.findCandles(request);
        return ResponseEntity.ok(candles);
    }
}
