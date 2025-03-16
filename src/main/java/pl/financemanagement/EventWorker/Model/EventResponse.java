package pl.financemanagement.EventWorker.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventResponse {

    private EventStatus status;
    private EventType eventType;

}
