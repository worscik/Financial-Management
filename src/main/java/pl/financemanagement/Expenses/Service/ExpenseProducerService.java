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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static pl.financemanagement.Expenses.Model.ExpenseMapper.*;

@Qualifier("expenseServiceImpl")
@Service
public class ExpenseProducerService implements ExpenseService {

    private static final String EXPENSE_CREATE_TOPIC = "expenses_topic";
    private static final String EXPENSE_UPDATE_TOPIC = "expenses_update_topic";

    private final ExpenseDao expenseDao;
    private final UserDao userDao;
    private final BankAccountDao bankAccountDao;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ExpenseProducerService(ExpenseDao expenseDao,
                                  UserDao userDao,
                                  BankAccountDao bankAccountDao,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
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

        BigDecimal newBalance = resolveOperationOnAccountAndCheckBalance(
                expenseRequest.getExpenseType(), bankAccount.getAccountBalance(), expenseRequest.getExpenseCost());

        bankAccount.setAccountBalance(newBalance);

        ExpenseCreateEvent expenseCreateEvent = buildExpenseCreateEvent(expenseRequest, bankAccount, userAccount);

        kafkaTemplate.send(EXPENSE_CREATE_TOPIC, expenseCreateEvent);

        return new ExpenseResponse(mapToDtoWithBankBalanceAndUserExternalId(
                expense, newBalance, UUID.fromString(userAccount.getExternalId())), true);
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email) {
        UserAccount userAccount = getUserAccount(email);
        BankAccount bankAccount = getBankAccountByUserOrThrow(userAccount.getId());
        Expense expense = expenseDao.findExpenseByExternalIdAndUserId(
                        expenseRequest.getExternalId(), userAccount.getId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID was not found."));

        BigDecimal newBalance = resolveOperationOnAccountAndCheckBalance(
                expenseRequest.getExpenseType(), bankAccount.getAccountBalance(), expenseRequest.getExpenseCost());
        bankAccount.setAccountBalance(newBalance);

        ExpenseUpdateEvent expenseUpdateEvent = buildExpenseUpdateEvent(expenseRequest, bankAccount, userAccount);

        kafkaTemplate.send(EXPENSE_UPDATE_TOPIC, expenseUpdateEvent);

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

    private BigDecimal resolveOperationOnAccountAndCheckBalance(ExpenseType expenseType,
                                                                BigDecimal accountBalance,
                                                                BigDecimal expenseCost) {
        if (accountBalance.compareTo(expenseCost) <= 0) {
            throw new NotEnoughMoneyForTransaction("Not Enough money for transaction ");
        }
        return switch (expenseType) {
            case EXPENSE -> accountBalance.subtract(expenseCost);
            case INCOME -> accountBalance.add(expenseCost);
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

    private UserAccount getUserAccount(String email) {
        return userDao.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    private ExpenseCreateEvent buildExpenseCreateEvent(ExpenseRequest expenseRequest,
                                                       BankAccount bankAccount,
                                                       UserAccount userAccount) {
        ExpenseCreateEvent expenseCreateEvent = new ExpenseCreateEvent();
        expenseCreateEvent.setExpenseType(expenseRequest.getExpenseType());
        expenseCreateEvent.setExpense(expenseRequest.getExpenseCost());
        expenseCreateEvent.setBankBalance(bankAccount.getAccountBalance());
        expenseCreateEvent.setUserId(userAccount.getId());
        expenseCreateEvent.setExternalId(expenseRequest.getExternalId());
        expenseCreateEvent.setExpenseCategory(expenseRequest.getExpenseCategory());
        return expenseCreateEvent;
    }

    private static ExpenseUpdateEvent buildExpenseUpdateEvent(ExpenseRequest expenseRequest,
                                                              BankAccount bankAccount,
                                                              UserAccount userAccount) {
        ExpenseUpdateEvent expenseUpdateEvent = new ExpenseUpdateEvent();
        expenseUpdateEvent.setExpenseType(expenseRequest.getExpenseType());
        expenseUpdateEvent.setExpense(expenseRequest.getExpenseCost());
        expenseUpdateEvent.setBankBalance(bankAccount.getAccountBalance());
        expenseUpdateEvent.setUserId(userAccount.getId());
        expenseUpdateEvent.setExternalId(expenseRequest.getExternalId());
        expenseUpdateEvent.setExpenseCategory(expenseRequest.getExpenseCategory());
        return expenseUpdateEvent;
    }
}
