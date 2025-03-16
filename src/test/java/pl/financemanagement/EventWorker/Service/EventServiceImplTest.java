package pl.financemanagement.EventWorker.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventResponse;
import pl.financemanagement.EventWorker.Model.EventType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Test
    public void testResolveRevenueStatsEvent() {
        // Tworzymy rzeczywisty bean dla revenueStatsService (możemy go też samemu zasymulować)
        EventHandler revenueStatsHandler = mock(EventHandler.class);
        EventResolver resolver = new EventResolver(revenueStatsHandler);

        // Upewniamy się, że dla eventu CALCULATE_REVENUE_STATS zwracany jest revenueStatsHandler
        EventHandler handler = resolver.resolveEventService(EventType.CALCULATE_REVENUE_STATS);
        assertNotNull(handler);
        assertEquals(revenueStatsHandler, handler);
    }
}