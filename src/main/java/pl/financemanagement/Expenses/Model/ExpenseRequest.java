package pl.financemanagement.Expenses.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class ExpenseRequest {

    private UUID externalId;
    @NotBlank(message = "expense category cannot be blank")
    private ExpenseCategory expenseCategory;
    @NotBlank(message = "expense type cannot be blank")
    private ExpenseType expenseType;
    @NotNull(message = "bankBalance cannot be null")
    private BigDecimal bankBalance;
    private BigDecimal expenseCost;
    private UUID bankAccountExternalId;

}
