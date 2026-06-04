package com.stockcandles;

import com.stockcandles.repository.StockCandleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cassandra.contact-points=127.0.0.1",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration"
})
class StockCandlesApplicationTests {

    @MockBean
    private StockCandleRepository stockCandleRepository;

    @Test
    void contextLoads() {
        // Verifies application context wiring without requiring a live Cassandra instance
    }
}
