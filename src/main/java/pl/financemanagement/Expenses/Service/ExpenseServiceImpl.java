package pl.financemanagement.Expenses.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;
import pl.financemanagement.Expenses.Repository.ExpenseDao;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.util.List;
import java.util.Optional;

import static pl.financemanagement.Expenses.Model.ExpenseMapper.mapToDto;
import static pl.financemanagement.Expenses.Model.ExpenseMapper.mapToExpense;

@Qualifier("expenseServiceImpl")
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseDao expenseDao;
    private final UserDao userDao;

    public ExpenseServiceImpl(ExpenseDao expenseDao, UserDao userDao) {
        this.expenseDao = expenseDao;
        this.userDao = userDao;
    }

    @Override
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email) {
        Optional<UserAccount> userAccount = userDao.findUserByEmail(email);
        if (userAccount.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        Expense expense = mapToExpense(expenseRequest);
        expense.setUser(userAccount.get());
        expense.setVersion(1);
        expenseDao.save(expense);
        return new ExpenseResponse(mapToDto(expense), true);
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email) {
        return null;
    }

    @Override
    public List<ExpenseDto> findExpenseByUserId(String externalId, String email) {
        return List.of();
    }

    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String email) {
        return null;
    }

    @Override
    public ExpenseResponse deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String userExternalId, String email) {
        return null;
    }
}
