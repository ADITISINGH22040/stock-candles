package com.stockcandles.enums;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public enum CandleInterval {

    ONE_MINUTE("1m", 1),
    FIVE_MINUTES("5m", 5),
    FIFTEEN_MINUTES("15m", 15),
    THIRTY_MINUTES("30m", 30),
    ONE_HOUR("1h", 60),
    ONE_DAY("1d", 1440);

    private final String code;
    private final int minutes;

    CandleInterval(String code, int minutes) {
        this.code = code;
        this.minutes = minutes;
    }

    public String getCode() {
        return code;
    }

    public static CandleInterval fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Timeframe is required");
        }
        return switch (code.trim().toLowerCase()) {
            case "1m" -> ONE_MINUTE;
            case "5m" -> FIVE_MINUTES;
            case "15m" -> FIFTEEN_MINUTES;
            case "30m" -> THIRTY_MINUTES;
            case "1h" -> ONE_HOUR;
            case "1d" -> ONE_DAY;
            default -> throw new IllegalArgumentException("Unsupported timeframe: " + code);
        };
    }

    public Instant floor(Instant timestamp, ZoneId zone) {
        ZonedDateTime dateTime = timestamp.atZone(zone);
        if (this == ONE_DAY) {
            return dateTime.toLocalDate().atStartOfDay(zone).toInstant();
        }
        int minute = dateTime.getMinute();
        int bucketMinute = (minute / minutes) * minutes;
        return dateTime.withMinute(bucketMinute)
                .withSecond(0)
                .withNano(0)
                .toInstant();
    }
}
