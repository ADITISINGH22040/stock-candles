package com.stockcandles.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockcandles.client.config.ClientProperties;
import com.stockcandles.client.dto.StockCandleAggregationResponse;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StockCandleApiClient {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ClientProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public StockCandleApiClient(ClientProperties properties) {
        this.properties = properties;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public StockCandleAggregationResponse fetchCandles(String symbol,
                                                       String timeframe,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate) {
        return fetchCandles(symbol, timeframe, startDate, endDate, 0, 200);
    }

    public StockCandleAggregationResponse fetchCandles(String symbol,
                                                       String timeframe,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate,
                                                       Integer page,
                                                       Integer size) {
        try {
            URI uri = URI.create(buildCandlesUrl(symbol, timeframe, startDate, endDate, page, size));
            HttpResponse<String> response = sendRequest(uri);
            if (response.statusCode() != 200) {
                throw new RuntimeException("API request failed with status " + response.statusCode() + ": " + response.body());
            }
            return parseResponse(response.body());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch candles", ex);
        }
    }

    protected String buildCandlesUrl(String symbol,
                                     String timeframe,
                                     LocalDateTime startDate,
                                     LocalDateTime endDate,
                                     Integer page,
                                     Integer size) {
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        String encodedTimeframe = URLEncoder.encode(timeframe, StandardCharsets.UTF_8);
        String encodedStart = URLEncoder.encode(startDate.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(endDate.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8);

        StringBuilder url = new StringBuilder(properties.getBaseUrl())
                .append("/api/v1/candles?symbol=")
                .append(encodedSymbol)
                .append("&timeframe=")
                .append(encodedTimeframe)
                .append("&start_date=")
                .append(encodedStart)
                .append("&end_date=")
                .append(encodedEnd);

        if (page != null) {
            url.append("&page=").append(page);
        }
        if (size != null) {
            url.append("&size=").append(size);
        }

        return url.toString();
    }

    protected StockCandleAggregationResponse parseResponse(String body) throws Exception {
        return objectMapper.readValue(body, StockCandleAggregationResponse.class);
    }

    protected HttpResponse<String> sendRequest(URI uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
