package pl.financemanagement.Revenue.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.time.Instant;

@Getter
@ToString
@Builder
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class RevenueStatsDto {

    private final long userId;
    private final Instant date;
    private final double totalIncomes;
    private final double totalExpenses;
    private final long numberOfTransactions;
    private final double averageTransactionValue;
    private final double averageIncomeValue;
    private final double averageExpenseValue;

}
