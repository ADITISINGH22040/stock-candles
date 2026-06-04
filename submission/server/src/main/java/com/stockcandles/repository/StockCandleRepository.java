package com.stockcandles.repository;

import com.stockcandles.model.StockCandle;
import com.stockcandles.model.StockCandleKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockCandleRepository extends CassandraRepository<StockCandle, StockCandleKey> {
}
