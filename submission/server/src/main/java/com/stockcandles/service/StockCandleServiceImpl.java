package com.stockcandles.service;

import com.stockcandles.dto.StockCandleAggregationResponse;
import com.stockcandles.dto.StockCandleQueryRequest;
import com.stockcandles.enums.CandleInterval;
import com.stockcandles.exception.ApiException;
import com.stockcandles.model.StockCandle;
import com.stockcandles.service.StockCandleAggregationHelper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockCandleServiceImpl implements StockCandleService {

    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CassandraTemplate cassandraTemplate;

    public StockCandleServiceImpl(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

    @Override
    @Cacheable(value = "stockCandles", key = "#request.symbol + '-' + #request.timeframe + '-' + #request.startDate + '-' + #request.endDate + '-' + #request.page + '-' + #request.size")
    public StockCandleAggregationResponse findCandles(StockCandleQueryRequest request) {
        validateRequest(request);

        LocalDateTime startDateTime = parseDateTime(request.getStartDate(), "start_date");
        LocalDateTime endDateTime = parseDateTime(request.getEndDate(), "end_date");
        if (endDateTime.isBefore(startDateTime)) {
            throw new ApiException("INVALID_DATE_RANGE", "end_date must be after or equal to start_date");
        }

        int page = request.getPage() == null ? 0 : request.getPage();
        int size = request.getSize() == null ? 200 : request.getSize();
        if (page < 0) {
            throw new ApiException("INVALID_PARAMETER", "page must be >= 0");
        }
        if (size <= 0 || size > 1000) {
            throw new ApiException("INVALID_PARAMETER", "size must be between 1 and 1000");
        }

        CandleInterval interval = parseInterval(request.getTimeframe());
        List<StockCandle> rawCandles = fetchCandles(request.getSymbol(), startDateTime, endDateTime);
        List<StockCandleAggregationResponse.Candle> candles = StockCandleAggregationHelper.aggregate(rawCandles, interval, ZONE);
        List<StockCandleAggregationResponse.Candle> pageContent = paginate(candles, page, size);

        return new StockCandleAggregationResponse(
                request.getSymbol(),
                interval.getCode(),
                pageContent,
                pageContent.size(),
                page,
                size);
    }

    private List<StockCandleAggregationResponse.Candle> paginate(List<StockCandleAggregationResponse.Candle> candles, int page, int size) {
        int fromIndex = page * size;
        if (fromIndex >= candles.size()) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(fromIndex + size, candles.size());
        return new ArrayList<>(candles.subList(fromIndex, toIndex));
    }

    private void validateRequest(StockCandleQueryRequest request) {
        if (request.getSymbol() == null || request.getSymbol().trim().isEmpty()) {
            throw new ApiException("MISSING_PARAMETER", "symbol is required");
        }
        if (request.getTimeframe() == null || request.getTimeframe().trim().isEmpty()) {
            throw new ApiException("MISSING_PARAMETER", "timeframe is required");
        }
        if (request.getStartDate() == null || request.getStartDate().trim().isEmpty()) {
            throw new ApiException("MISSING_PARAMETER", "start_date is required");
        }
        if (request.getEndDate() == null || request.getEndDate().trim().isEmpty()) {
            throw new ApiException("MISSING_PARAMETER", "end_date is required");
        }
    }

    private LocalDateTime parseDateTime(String text, String fieldName) {
        try {
            return LocalDateTime.parse(text, DATE_TIME_FORMATTER);
        } catch (Exception ex) {
            throw new ApiException("INVALID_DATE_FORMAT", fieldName + " must be in yyyy-MM-dd HH:mm:ss format");
        }
    }

    private CandleInterval parseInterval(String timeframe) {
        try {
            return CandleInterval.fromCode(timeframe);
        } catch (IllegalArgumentException ex) {
            throw new ApiException("UNSUPPORTED_TIMEFRAME", ex.getMessage());
        }
    }

    private List<StockCandle> fetchCandles(String symbol, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<StockCandle> candles = new ArrayList<>();
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Instant rangeStart = date.equals(startDate)
                    ? startDateTime.atZone(ZONE).toInstant()
                    : date.atStartOfDay(ZONE).toInstant();

            Instant rangeEnd = date.equals(endDate)
                    ? endDateTime.atZone(ZONE).toInstant()
                    : date.plusDays(1).atStartOfDay(ZONE).toInstant();

            Query query = Query.query(
                            Criteria.where("symbol").is(symbol),
                            Criteria.where("trading_date").is(date),
                            Criteria.where("candle_time").gte(rangeStart),
                            date.equals(endDate)
                                    ? Criteria.where("candle_time").lte(rangeEnd)
                                    : Criteria.where("candle_time").lt(rangeEnd))
                    .sort(Sort.by(Sort.Order.asc("candle_time")));

            candles.addAll(cassandraTemplate.select(query, StockCandle.class));
        }
        return candles.stream()
                .sorted(Comparator.comparing(it -> it.getKey().getCandleTime()))
                .collect(Collectors.toList());
    }

}
