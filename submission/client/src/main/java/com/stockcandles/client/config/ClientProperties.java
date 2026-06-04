package com.stockcandles.client.config;

public class ClientProperties {

    private final String baseUrl;

    public ClientProperties(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static ClientProperties fromEnvironment() {
        String baseUrl = System.getenv().getOrDefault("STOCK_API_BASE_URL", "http://localhost:8080");
        return new ClientProperties(baseUrl);
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
