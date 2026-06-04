package com.stockcandles.client;

import com.stockcandles.client.cli.CandleQueryCommand;
import com.stockcandles.client.config.ClientProperties;
import com.stockcandles.client.service.StockCandleApiClient;

public class StockCandlesClientApplication {

    public static void main(String[] args) {
        ClientProperties properties = ClientProperties.fromEnvironment();
        StockCandleApiClient apiClient = new StockCandleApiClient(properties);
        CandleQueryCommand command = new CandleQueryCommand(apiClient);
        command.run(args);
    }
}
