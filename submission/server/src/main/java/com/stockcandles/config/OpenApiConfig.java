package com.stockcandles.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stockCandlesOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Candles API")
                        .description("REST API for stock market candle data aggregation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Stock Candles Team")));
    }
}
