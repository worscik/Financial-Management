package pl.financemanagement.Expenses.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service("expenseServiceDemo")
public class ExpenseServiceDemo implements ExpenseService {

    private final static UUID UUID_NUMBER = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2945");
    private final static UUID USER_EXTERNAL_ID = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2942");

    @Override
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email) {
        return new ExpenseResponse(buildExpenseDto(), true);
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email) {
        return new ExpenseResponse(buildUpsertedExpenseDto(), true);
    }

    @Override
    public List<ExpenseDto> findExpenseByUserNameAndExternalId(String email, UUID bankAccountExternalId) {
        return List.of(buildExpenseDto());
    }


    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String userExternalId, UUID bankAccountExternalId) {
        return new ExpenseResponse(buildUpsertedExpenseDto(), true);
    }

    @Override
    public void deleteExpenseByUserExternalIdAndExpenseExternalId(String email, UUID bankAccountExternalId) {
    }

    @Override
    public List<ExpenseCategory> getExpensesCategories() {
        return List.of();
    }

    private ExpenseDto buildExpenseDto() {
        return new ExpenseDto.Builder()
                .userExternalId(USER_EXTERNAL_ID)
                .expenseType(ExpenseType.INCOME)
                .createdOn(Instant.now())
                .externalId(UUID_NUMBER)
                .expenseCategory(ExpenseCategory.FOOD)
                .bankBalance(BigDecimal.valueOf(10000))
                .build();
    }

    private ExpenseDto buildUpsertedExpenseDto() {
        return new ExpenseDto.Builder()
                .userExternalId(USER_EXTERNAL_ID)
                .expenseType(ExpenseType.EXPENSE)
                .createdOn(Instant.now())
                .externalId(UUID_NUMBER)
                .expenseCategory(ExpenseCategory.WORK)
                .bankBalance(BigDecimal.valueOf(1200))
                .build();
    }


}
