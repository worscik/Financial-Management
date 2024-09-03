package pl.financemanagement.Expenses.Service;

import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Optional<ExpenseResponse> createExpense(ExpenseRequest expenseRequest);

    Optional<ExpenseResponse> updateExpense(ExpenseRequest expenseRequest);

    List<ExpenseDto> findExpenseByUserId(String externalId);

    Optional<ExpenseResponse> findExpenseByIdAndUserId(String expenseExternalId, String userExternalId);

    boolean deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String userExternalId);

}
