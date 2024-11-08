package pl.financemanagement.Expenses.Service;

import pl.financemanagement.Expenses.Model.ExpenseCategory;
import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email);

    ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email);

    List<ExpenseDto> findExpenseByUserName(String email);

    ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String email);

    void deleteExpenseByUserExternalIdAndExpenseExternalId(
            String expenseExternalId, String email);

    List<ExpenseCategory> getExpensesCategories();

}
