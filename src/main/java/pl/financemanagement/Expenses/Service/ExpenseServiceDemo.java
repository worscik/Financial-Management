package pl.financemanagement.Expenses.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Qualifier("expenseServiceDemo")
@Service
public class ExpenseServiceDemo implements ExpenseService {

    private final static UUID UUID_NUMBER = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2945");
    private final static UUID USER_EXTERNAL_ID = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2942");
    private final static long USER_ID = 999L;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email) {
        return new ExpenseResponse(buildExpenseDto(), true);
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email) {
        return new ExpenseResponse(buildUpsertedExpenseDto(), true);
    }

    @Override
    public List<ExpenseDto> findExpenseByUserName(String email) {
        return List.of(buildExpenseDto());
    }


    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String userExternalId) {
        return new ExpenseResponse(buildUpsertedExpenseDto(), true);
    }

    @Override
    public void deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId,
                                                                     String email) {
    }

    private ExpenseDto buildExpenseDto() {
        return new ExpenseDto.Builder()
                .userId(USER_EXTERNAL_ID)
                .expenseType(ExpenseType.INCOME)
                .createdOn(Instant.now())
                .externalId(UUID_NUMBER)
                .expenseCategory(ExpenseCategory.FOOD)
                .bankBalance(BigDecimal.valueOf(10000))
                .build();
    }

    private ExpenseDto buildUpsertedExpenseDto() {
        return new ExpenseDto.Builder()
                .userId(USER_EXTERNAL_ID)
                .expenseType(ExpenseType.EXPENSE)
                .createdOn(Instant.now())
                .externalId(UUID_NUMBER)
                .expenseCategory(ExpenseCategory.WORK)
                .bankBalance(BigDecimal.valueOf(1200))
                .build();
    }


}
