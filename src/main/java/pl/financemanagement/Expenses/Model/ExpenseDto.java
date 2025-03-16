package pl.financemanagement.Expenses.Model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ExpenseDto {

    private UUID externalId;
    private Instant createdOn;
    private ExpenseCategory expenseCategory;
    private ExpenseType expenseType;
    private UUID userExternalId;
    private BigDecimal bankBalance;

}
