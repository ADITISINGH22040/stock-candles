package com.stockcandles.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CassandraStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(CassandraStartupLogger.class);

    @Value("${spring.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.cassandra.port}")
    private int port;

    @Value("${spring.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.cassandra.local-datacenter}")
    private String localDatacenter;

    @Value("${spring.application.name}")
    private String applicationName;

    @EventListener(ApplicationReadyEvent.class)
    public void logCassandraConnectionDetails() {
        log.info("Starting {} with Cassandra configuration:", applicationName);
        log.info("  Cassandra host: {}", contactPoints);
        log.info("  Cassandra port: {}", port);
        log.info("  Cassandra keyspace: {}", keyspaceName);
        log.info("  Local datacenter: {}", localDatacenter);
    }
}
