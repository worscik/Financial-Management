package pl.financemanagement.Revenue.Model;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public enum AggregationPeriod {

    NOW(Duration.ofDays(0)),
    ONE_DAY(Duration.ofDays(1)),
    SEVEN_DAYS(Duration.ofDays(7)),
    THIRTY_DAYS(Duration.ofDays(30));

    private final Duration duration;

    AggregationPeriod(Duration duration) {
        this.duration = duration;
    }

    public Instant calculateFromDate(Instant now) {
        return now.minus(duration);
    }

}
