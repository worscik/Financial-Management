package pl.financemanagement.EventWorker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventType;
import pl.financemanagement.EventWorker.Service.EventServiceImpl;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventServiceImpl eventService;

    @Autowired
    public EventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/testEvent")
    void testEvent() {
        Event event = Event.builder()
                .id(1L)
                .eventType(EventType.CALCULATE_REVENUE_STATS)
                .userId(1L)
                .build();
        eventService.execute(event);
    }

}
