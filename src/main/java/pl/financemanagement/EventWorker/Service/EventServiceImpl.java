package pl.financemanagement.EventWorker.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventResponse;

@EnableKafka
@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventResolver eventResolver;

    public EventServiceImpl(EventResolver eventResolver) {
        this.eventResolver = eventResolver;
    }

    @KafkaListener(topics = "event", groupId = "event-group")
    @Override
    public EventResponse execute(Event event) {
        LOGGER.info("Received event: {}", event);
        return eventResolver.resolveEventService(event.getEventType()).performAction(event);
    }

}
