package pl.financemanagement.Expenses.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountDao;
import pl.financemanagement.Expenses.Model.*;
import pl.financemanagement.Expenses.Model.exceptions.NotEnoughMoneyForTransaction;
import pl.financemanagement.Expenses.Repository.ExpenseDao;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static pl.financemanagement.Expenses.Model.ExpenseMapper.mapToDto;
import static pl.financemanagement.Expenses.Model.ExpenseMapper.mapToExpense;
import static pl.financemanagement.Expenses.Model.ExpenseType.EXPENSE;

@Qualifier("expenseServiceImpl")
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseDao expenseDao;
    private final UserDao userDao;
    private final BankAccountDao bankAccountDao;

    public ExpenseServiceImpl(ExpenseDao expenseDao, UserDao userDao, BankAccountDao bankAccountDao) {
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.bankAccountDao = bankAccountDao;
    }

    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email) {
        Optional<UserAccount> userAccount = userDao.findUserByEmail("demo@example.com");
        if (userAccount.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }

        Expense expense = createExpenseFromRequest(expenseRequest, userAccount.get());
        BankAccount bankAccount = findBankAccount(userAccount.get().getId());

        if (Boolean.FALSE.equals(hasSufficientBalance(bankAccount.getAccountBalance(), expense.getExpense()))) {
            throw new NotEnoughMoneyForTransaction("Not Enough money for transaction ");
        }

        BigDecimal newBalance = resolveOperationOnAccount(expenseRequest.getExpenseType(), bankAccount, expenseRequest);
        bankAccount.setAccountBalance(newBalance);
        expenseDao.save(expense);
        bankAccountDao.save(bankAccount);
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

    private BigDecimal resolveOperationOnAccount(ExpenseType expenseType, BankAccount account, ExpenseRequest request) {
        if (expenseType.equals(EXPENSE)) {
            return account.getAccountBalance().subtract(request.getAmount());
        }
        return account.getAccountBalance().add(request.getAmount());
    }

    private Expense createExpenseFromRequest(ExpenseRequest expenseRequest, UserAccount userAccount) {
        Expense expense = mapToExpense(expenseRequest);
        expense.setUser(userAccount);
        expense.setVersion(1);
        return expense;
    }

    private BankAccount findBankAccount(long userId) {
        Optional<BankAccount> bankAccount = Optional.ofNullable(bankAccountDao.findAccountById(userId));
        if (bankAccount.isEmpty()) {
            throw new BankAccountNotFoundException("Account for user " + userId + " not found");
        }
        return bankAccount.get();
    }

    private boolean hasSufficientBalance(BigDecimal bankBalance, BigDecimal expenseAmount) {
        return bankBalance.compareTo(expenseAmount) >= 0;
    }

}
