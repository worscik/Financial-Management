package pl.financemanagement.Expenses.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;

import java.util.List;
import java.util.Optional;

@Qualifier("expenseServiceImpl")
@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Override
    public Optional<ExpenseResponse> createExpense(ExpenseRequest expenseRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<ExpenseResponse> updateExpense(ExpenseRequest expenseRequest) {
        return Optional.empty();
    }

    @Override
    public List<ExpenseDto> findExpenseByUserId(String externalId) {
        return List.of();
    }

    @Override
    public Optional<ExpenseResponse> findExpenseByIdAndUserId(String expenseExternalId, String userExternalId) {
        return Optional.empty();
    }

    @Override
    public boolean deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String userExternalId) {
        return false;
    }
}
