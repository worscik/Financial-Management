package pl.financemanagement.EventWorker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventType;
import pl.financemanagement.EventWorker.Service.EventServiceImpl;

@RestController
@RequestMapping("/event")
@EnableKafka
public class EventController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public EventController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/testEvent")
    void testEvent() {
        Event event = Event.builder()
                .id(1L)
                .eventType(EventType.CALCULATE_REVENUE_STATS)
                .userId(1L)
                .build();
        kafkaTemplate.send("event", event);
    }

}
