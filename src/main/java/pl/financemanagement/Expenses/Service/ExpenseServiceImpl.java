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
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest) {
        return null;    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest) {
        return null;    }

    @Override
    public List<ExpenseDto> findExpenseByUserId(String externalId) {
        return List.of();
    }

    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String userExternalId) {
        return null;
    }

    @Override
    public boolean deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String userExternalId) {
        return false;
    }
}
