package pl.financemanagement.Revenue.Service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.EventWorker.Model.Event;
import pl.financemanagement.EventWorker.Model.EventResponse;
import pl.financemanagement.EventWorker.Model.EventStatus;
import pl.financemanagement.EventWorker.Model.EventType;
import pl.financemanagement.EventWorker.Service.EventHandler;
import pl.financemanagement.Expenses.Repository.ExpenseRepository;

@Service
@Qualifier("revenueStatsService")
public class RevenueStatsService implements EventHandler {

    private final ExpenseRepository expenseRepository;

    public RevenueStatsService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public EventResponse performAction(Event event) {
        return new EventResponse(EventStatus.DONE, EventType.CALCULATE_REVENUE_STATS);
    }

}
