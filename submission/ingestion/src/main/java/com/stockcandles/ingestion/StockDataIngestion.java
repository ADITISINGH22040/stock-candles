package com.stockcandles.ingestion;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.BatchableStatement;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * One-time utility: parse stock_data.csv and insert into stock_keyspace.stock_candles.
 */
public class StockDataIngestion {

    private static final String DEFAULT_CSV = "submission/data/stock_data.csv";
    private static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
    private static final int BATCH_SIZE = 50;
    private static final int PROGRESS_EVERY = 10_000;

    public static void main(String[] args) throws Exception {
        Path csvFile = resolveCsvPath(args);

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .withLocalDatacenter("datacenter1")
                .withKeyspace("stock_keyspace")
                .build();
             BufferedReader reader = Files.newBufferedReader(csvFile)) {

            PreparedStatement insert = session.prepare(
                    "INSERT INTO stock_candles (symbol, trading_date, candle_time, open, high, low, close, volume) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            String header = reader.readLine();
            if (header == null) {
                throw new IllegalStateException("CSV is empty: " + csvFile);
            }

            long inserted = 0;
            List<BatchableStatement<?>> batch = new ArrayList<>(BATCH_SIZE);
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                batch.add(insert.bind(parseRow(line)));
                if (batch.size() >= BATCH_SIZE) {
                    session.execute(BatchStatement.newInstance(BatchType.UNLOGGED, batch));
                    inserted += batch.size();
                    batch.clear();
                    if (inserted % PROGRESS_EVERY == 0) {
                        System.out.println("Ingested " + inserted + " rows...");
                    }
                }
            }

            if (!batch.isEmpty()) {
                session.execute(BatchStatement.newInstance(BatchType.UNLOGGED, batch));
                inserted += batch.size();
            }

            System.out.println("Ingestion complete. Total rows inserted: " + inserted);
        }
    }

    private static Object[] parseRow(String line) {
        String[] c = line.split(",", -1);
        LocalDateTime dt = LocalDateTime.parse(c[1].trim(), DATETIME);
        LocalDate tradingDate = dt.toLocalDate();
        Instant candleTime = dt.atZone(ZONE).toInstant();
        return new Object[]{
                c[0].trim(),
                tradingDate,
                candleTime,
                new BigDecimal(c[2].trim()),
                new BigDecimal(c[3].trim()),
                new BigDecimal(c[4].trim()),
                new BigDecimal(c[5].trim()),
                Long.parseLong(c[6].trim())
        };
    }

    private static Path resolveCsvPath(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if ("--file".equals(args[i])) {
                return Path.of(args[i + 1]);
            }
        }
        Path defaultPath = Path.of(DEFAULT_CSV);
        if (Files.isRegularFile(defaultPath)) {
            return defaultPath;
        }
        return Path.of("data", "stock_data.csv");
    }
}
