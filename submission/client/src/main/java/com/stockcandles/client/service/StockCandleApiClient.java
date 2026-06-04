package com.stockcandles.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockcandles.client.config.ClientProperties;
import com.stockcandles.client.dto.StockCandleResponse;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class StockCandleApiClient {

    private final ClientProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public StockCandleApiClient(ClientProperties properties) {
        this.properties = properties;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public List<StockCandleResponse> fetchCandles(String symbol, LocalDate tradingDate) {
        // Business logic to be implemented
        return Collections.emptyList();
    }

    protected String buildCandlesUrl(String symbol, LocalDate tradingDate) {
        String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        String encodedDate = URLEncoder.encode(tradingDate.toString(), StandardCharsets.UTF_8);
        return properties.getBaseUrl()
                + "/api/v1/candles?symbol="
                + encodedSymbol
                + "&tradingDate="
                + encodedDate;
    }

    protected List<StockCandleResponse> parseResponse(String body) throws Exception {
        return objectMapper.readValue(body, new TypeReference<>() {
        });
    }

    protected HttpResponse<String> sendRequest(URI uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
