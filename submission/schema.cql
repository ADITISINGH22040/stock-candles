-- =============================================================================
-- Stock Candles — Apache Cassandra schema (local development)
-- =============================================================================

-- Keyspace for local development: single datacenter, replication factor 1.
CREATE KEYSPACE IF NOT EXISTS stock_keyspace
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

-- -----------------------------------------------------------------------------
-- stock_candles
-- -----------------------------------------------------------------------------
-- Partition key (symbol, trading_date):
--   The API loads candles for one symbol on one trading day (or a bounded date
--   range per symbol). Co-locating rows by symbol + trading_date keeps each
--   query on a single partition and avoids scanning all symbols or all dates.
--
-- Clustering key (candle_time ASC):
--   Within a partition, candles are stored in time order. Range filters and
--   ordered reads (e.g. from 09:30 to 16:00) use the clustering key efficiently
--   without a full partition scan or application-side sort.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stock_keyspace.stock_candles (
    symbol text,
    trading_date date,
    candle_time timestamp,
    open decimal,
    high decimal,
    low decimal,
    close decimal,
    volume bigint,
    PRIMARY KEY ((symbol, trading_date), candle_time)
) WITH CLUSTERING ORDER BY (candle_time ASC);
