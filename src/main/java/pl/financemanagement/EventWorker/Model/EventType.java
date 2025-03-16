package pl.financemanagement.EventWorker.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum EventType {

    CALCULATE_REVENUE_STATS(1);

    private final int eventId;

}
