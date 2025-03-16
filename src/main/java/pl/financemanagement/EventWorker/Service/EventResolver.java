package pl.financemanagement.EventWorker.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.EventWorker.Model.EventType;

@Service
public class EventResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventResolver.class);

    private final EventHandler revenueStatsService;

    public EventResolver(@Qualifier("revenueStatsService") EventHandler revenueStatsService) {
        this.revenueStatsService = revenueStatsService;
    }

    public EventHandler resolveEventService(EventType eventType) {
        return switch (eventType) {
            case CALCULATE_REVENUE_STATS -> revenueStatsService;
            default -> {
                LOGGER.error("EventResolver: Unknown event type: " + eventType);
                throw new IllegalArgumentException("Unknown event type: " + eventType);
            }
        };
    }

}
