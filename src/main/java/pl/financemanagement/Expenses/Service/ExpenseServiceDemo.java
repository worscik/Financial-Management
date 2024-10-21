package pl.financemanagement.Expenses.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Qualifier("expenseServiceDemo")
@Service
public class ExpenseServiceDemo implements ExpenseService{

    private final static UUID UUID_NUMBER = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2945");
    private final static long USER_ID = 999L;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest) {
        return new ExpenseResponse(buildExpenseDto(), true);
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest) {
        return new ExpenseResponse(buildUpsertedExpenseDto(), true);
    }

    @Override
    public List<ExpenseDto> findExpenseByUserId(String externalId) {
        return List.of(buildExpenseDto(), buildUpsertedExpenseDto());
    }


    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String userExternalId) {
        return new ExpenseResponse(buildUpsertedExpenseDto(), true);
    }

    @Override
    public boolean deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String userExternalId) {
        return true;
    }

    private ExpenseDto buildExpenseDto() {
        return new ExpenseDto.Builder()
                .userId(USER_ID)
                .expenseType(ExpenseType.INCOME)
                .createdOn(Instant.now())
                .externalId(UUID_NUMBER)
                .expenseCategory(ExpenseCategory.FOOD)
                .amount(BigDecimal.valueOf(10000))
                .build();
    }

    private ExpenseDto buildUpsertedExpenseDto() {
        return new ExpenseDto.Builder()
                .userId(USER_ID)
                .expenseType(ExpenseType.EXPENSE)
                .createdOn(Instant.now())
                .externalId(UUID_NUMBER)
                .expenseCategory(ExpenseCategory.WORK)
                .amount(BigDecimal.valueOf(1200))
                .build();
    }


}
