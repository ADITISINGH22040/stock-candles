package com.stockcandles.controller;

import com.stockcandles.service.StockCandleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockCandleController.class)
class StockCandleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockCandleService stockCandleService;

    @Test
    void getCandles_returnsOk() throws Exception {
        when(stockCandleService.findCandles(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/candles")
                        .param("symbol", "AAPL")
                        .param("tradingDate", "2024-01-15"))
                .andExpect(status().isOk());
    }
}
