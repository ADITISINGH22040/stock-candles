package com.stockcandles.service;

import com.stockcandles.dto.StockCandleAggregationResponse;
import com.stockcandles.dto.StockCandleQueryRequest;

public interface StockCandleService {

    StockCandleAggregationResponse findCandles(StockCandleQueryRequest request);
}
