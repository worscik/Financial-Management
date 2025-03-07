package pl.financemanagement.Expenses.Model.KafkaExpenseEvents;

import lombok.*;
import pl.financemanagement.Expenses.Model.ExpenseCategory;
import pl.financemanagement.Expenses.Model.ExpenseType;
import pl.financemanagement.User.UserModel.UserAccount;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseCreateEvent {

    private UUID externalId;
    private ExpenseCategory expenseCategory;
    private ExpenseType expenseType;
    private BigDecimal bankBalance;
    private BigDecimal expense;
    private UserAccount userAccount;
    private UUID bankAccountExternalId;

}
