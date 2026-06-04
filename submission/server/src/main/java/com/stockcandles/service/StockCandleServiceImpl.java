package com.stockcandles.service;

import com.stockcandles.dto.StockCandleQueryRequest;
import com.stockcandles.dto.StockCandleResponse;
import com.stockcandles.repository.StockCandleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StockCandleServiceImpl implements StockCandleService {

    private final StockCandleRepository stockCandleRepository;

    public StockCandleServiceImpl(StockCandleRepository stockCandleRepository) {
        this.stockCandleRepository = stockCandleRepository;
    }

    @Override
    @Cacheable(value = "stockCandles", key = "#request.symbol + '-' + #request.tradingDate")
    public List<StockCandleResponse> findCandles(StockCandleQueryRequest request) {
        // Business logic to be implemented
        return Collections.emptyList();
    }
}
