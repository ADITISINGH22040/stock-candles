package com.stockcandles.util;

import com.stockcandles.dto.StockCandleResponse;
import com.stockcandles.model.StockCandle;

public final class StockCandleMapper {

    private StockCandleMapper() {
    }

    public static StockCandleResponse toResponse(StockCandle entity) {
        StockCandleResponse response = new StockCandleResponse();
        if (entity.getKey() != null) {
            response.setSymbol(entity.getKey().getSymbol());
            response.setTradingDate(entity.getKey().getTradingDate());
            response.setCandleTime(entity.getKey().getCandleTime());
        }
        response.setOpen(entity.getOpen());
        response.setHigh(entity.getHigh());
        response.setLow(entity.getLow());
        response.setClose(entity.getClose());
        response.setVolume(entity.getVolume());
        return response;
    }
}
