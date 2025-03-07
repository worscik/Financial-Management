package pl.financemanagement.Expenses.Service;

import pl.financemanagement.Expenses.Model.ExpenseCategory;
import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email);

    ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email);

    List<ExpenseDto> findExpenseByUserNameAndExternalId(String email, UUID expenseExternalId);

    ExpenseResponse findExpenseByIdAndUserId(String email, UUID expenseExternalId);

    void deleteExpenseByUserExternalIdAndExpenseExternalId(String email, UUID expenseExternalId);

    List<ExpenseCategory> getExpensesCategories();

}
