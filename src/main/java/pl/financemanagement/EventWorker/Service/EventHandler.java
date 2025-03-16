package pl.financemanagement.EventWorker.Service;

import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventResponse;

public interface EventHandler {

    EventResponse performAction(Event event);

}
