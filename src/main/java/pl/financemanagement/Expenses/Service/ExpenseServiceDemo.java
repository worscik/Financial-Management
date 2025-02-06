package pl.financemanagement.Expenses.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service("expenseServiceDemo")
public class ExpenseServiceDemo implements ExpenseService {

    private final static String UUID_NUMBER = "2ae2eeba-7980-458c-9677-8bc41abf2945";
    private final static String USER_EXTERNAL_ID = "2ae2eeba-7980-458c-9677-8bc41abf2942";
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

    @Override
    public List<ExpenseCategory> getExpensesCategories() {
        return List.of();
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
