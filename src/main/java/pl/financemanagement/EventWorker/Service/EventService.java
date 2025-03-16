package pl.financemanagement.EventWorker.Service;

import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventResponse;

public interface EventService {

    EventResponse execute(Event event);

}
