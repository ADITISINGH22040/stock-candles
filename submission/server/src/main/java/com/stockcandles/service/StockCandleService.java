package com.stockcandles.service;

import com.stockcandles.dto.StockCandleQueryRequest;
import com.stockcandles.dto.StockCandleResponse;

import java.util.List;

public interface StockCandleService {

    List<StockCandleResponse> findCandles(StockCandleQueryRequest request);
}
