package com.stockcandles;

import com.datastax.oss.driver.api.core.CqlSession;
import com.stockcandles.repository.StockCandleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cassandra.contact-points=127.0.0.1",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration"
})
class StockCandlesApplicationTests {

    @MockBean
    private StockCandleRepository stockCandleRepository;

    @MockBean
    private CassandraTemplate cassandraTemplate;

    @MockBean
    private CqlSession cqlSession;

    @Test
    void contextLoads() {
        // Verifies application context wiring without requiring a live Cassandra instance
    }
}
