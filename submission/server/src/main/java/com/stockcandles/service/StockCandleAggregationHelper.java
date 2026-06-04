package com.stockcandles.service;

import com.stockcandles.dto.StockCandleAggregationResponse;
import com.stockcandles.enums.CandleInterval;
import com.stockcandles.model.StockCandle;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StockCandleAggregationHelper {

    private StockCandleAggregationHelper() {
    }

    public static List<StockCandleAggregationResponse.Candle> aggregate(List<StockCandle> candles,
                                                                         CandleInterval interval,
                                                                         ZoneId zone) {
        Map<Instant, List<StockCandle>> buckets = new LinkedHashMap<>();
        for (StockCandle candle : candles) {
            Instant bucket = interval.floor(candle.getKey().getCandleTime(), zone);
            buckets.computeIfAbsent(bucket, key -> new ArrayList<>()).add(candle);
        }

        return buckets.entrySet().stream()
                .map(entry -> buildBucket(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private static StockCandleAggregationResponse.Candle buildBucket(Instant bucketDatetime,
                                                                     List<StockCandle> bucketCandles) {
        bucketCandles.sort(Comparator.comparing(c -> c.getKey().getCandleTime()));

        var first = bucketCandles.get(0);
        var last = bucketCandles.get(bucketCandles.size() - 1);

        var high = bucketCandles.stream()
                .map(StockCandle::getHigh)
                .max(Comparator.naturalOrder())
                .orElse(first.getHigh());

        var low = bucketCandles.stream()
                .map(StockCandle::getLow)
                .min(Comparator.naturalOrder())
                .orElse(first.getLow());

        var volume = bucketCandles.stream()
                .mapToLong(c -> c.getVolume() == null ? 0L : c.getVolume())
                .sum();

        return new StockCandleAggregationResponse.Candle(
                bucketDatetime,
                first.getOpen(),
                high,
                low,
                last.getClose(),
                volume);
    }
}
