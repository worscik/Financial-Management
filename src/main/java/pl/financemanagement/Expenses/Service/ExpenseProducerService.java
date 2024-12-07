package pl.financemanagement.Expenses.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountDao;
import pl.financemanagement.Expenses.Model.*;
import pl.financemanagement.Expenses.Model.exceptions.ExpenseNotFoundException;
import pl.financemanagement.Expenses.Model.exceptions.NotEnoughMoneyForTransaction;
import pl.financemanagement.Expenses.Repository.ExpenseDao;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static pl.financemanagement.Expenses.Model.ExpenseMapper.*;

@Qualifier("expenseServiceImpl")
@Service
public class ExpenseProducerService implements ExpenseService {

    private static final String EXPENSE_TOPIC = "expenses_topic";

    private final ExpenseDao expenseDao;
    private final UserDao userDao;
    private final BankAccountDao bankAccountDao;
    private final KafkaTemplate<String, ExpenseEvent> kafkaTemplate;

    public ExpenseProducerService(ExpenseDao expenseDao,
                                  UserDao userDao,
                                  BankAccountDao bankAccountDao,
                                  KafkaTemplate<String, ExpenseEvent> kafkaTemplate) {
        this.expenseDao = expenseDao;
        this.userDao = userDao;
        this.bankAccountDao = bankAccountDao;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email) {
        UserAccount userAccount = getUserAccount(email);

        Expense expense = createExpenseFromRequest(expenseRequest, userAccount);
        BankAccount bankAccount = getBankAccountByUserOrThrow(userAccount.getId());

        if (hasNoSufficientBalance(bankAccount.getAccountBalance(), expense.getExpense())) {
            throw new NotEnoughMoneyForTransaction("Not Enough money for transaction ");
        }

        BigDecimal newBalance = resolveOperationOnAccount(expenseRequest.getExpenseType(), bankAccount, expenseRequest);
        bankAccount.setAccountBalance(newBalance);

        ExpenseEvent expenseEvent = new ExpenseEvent(UUID.randomUUID(), expenseRequest.getExpenseCategory(),
                expenseRequest.getExpenseType(), bankAccount.getAccountBalance(), expenseRequest.getExpense(),
                userAccount.getId());

        kafkaTemplate.send(EXPENSE_TOPIC, expenseEvent);

        return new ExpenseResponse(mapToDtoWithBankBalanceAndUserExternalId(
                expense, newBalance, UUID.fromString(userAccount.getExternalId())), true);
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email) {
        UserAccount userAccount = getUserAccount(email);

        Expense expense = expenseDao.findExpenseByExternalIdAndUserId(
                        expenseRequest.getExternalId(), userAccount.getId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID was not found."));

        BankAccount bankAccount = getBankAccountByUserOrThrow(userAccount.getId());

        BigDecimal newBalance = resolveOperationOnAccount(expenseRequest.getExpenseType(), bankAccount, expenseRequest);
        bankAccount.setAccountBalance(newBalance);

        Expense expenseToUpdate = mapToExpense(expenseRequest);
        expenseToUpdate.setModifyOn(Instant.now());
        expenseDao.saveExpense(expenseToUpdate);
        return new ExpenseResponse(mapToDtoWithBankBalanceAndUserExternalId(
                expense, bankAccount.getAccountBalance(), UUID.fromString(userAccount.getExternalId())), true);
    }

    @Override
    public List<ExpenseDto> findExpenseByUserName(String email) {
        UserAccount userAccount = getUserAccount(email);
        BankAccount bankAccount = getBankAccountByUserOrThrow(userAccount.getId());

        return expenseDao.findAllExpensesByUserId(userAccount.getId()).stream()
                .map(expense -> mapToDtoWithBankBalanceAndUserExternalId(
                        expense, bankAccount.getAccountBalance(), UUID.fromString(userAccount.getExternalId())))
                .toList();
    }

    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String email) {
        UserAccount userAccount = getUserAccount(email);
        //TODO secure cast uuid
        Expense expense = expenseDao.findExpenseByExternalIdAndUserId(UUID.fromString(expenseExternalId), userAccount.getId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID was not found."));
        return new ExpenseResponse(mapToDto(expense), true);
    }

    @Override
    public void deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String email) {
        UserAccount userAccount = getUserAccount(email);

        Expense expense = expenseDao.findExpenseByExternalIdAndUserId(UUID.fromString(expenseExternalId), userAccount.getId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID was not found."));
        expenseDao.deleteExpense(expense);
    }

    @Override
    public List<ExpenseCategory> getExpensesCategories() {
        return Arrays.stream(ExpenseCategory.values()).toList();
    }

    private BigDecimal resolveOperationOnAccount(ExpenseType expenseType, BankAccount account, ExpenseRequest request) {
        return switch (expenseType) {
            case EXPENSE -> account.getAccountBalance().subtract(request.getExpense());
            case INCOME -> account.getAccountBalance().add(request.getExpense());
            default -> throw new IllegalArgumentException("Unsupported expense type: " + expenseType);
        };
    }

    private Expense createExpenseFromRequest(ExpenseRequest expenseRequest, UserAccount userAccount) {
        Expense expense = mapToExpense(expenseRequest);
        expense.setUser(userAccount.getId());
        return expense;
    }

    private BankAccount getBankAccountByUserOrThrow(long userId) {
        return bankAccountDao.findAccountByUserId(userId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account for user " + userId + " not found"));
    }

    private boolean hasNoSufficientBalance(BigDecimal bankBalance, BigDecimal expenseAmount) {
        return bankBalance.compareTo(expenseAmount) <= 0;
    }

    private UserAccount getUserAccount(String email) {
        return userDao.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

}
