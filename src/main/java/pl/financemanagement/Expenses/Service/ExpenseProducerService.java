package pl.financemanagement.Expenses.Service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountRepository;
import pl.financemanagement.Expenses.Model.*;
import pl.financemanagement.Expenses.Model.KafkaExpenseEvents.ExpenseCreateEvent;
import pl.financemanagement.Expenses.Model.KafkaExpenseEvents.ExpenseDeleteEvent;
import pl.financemanagement.Expenses.Model.KafkaExpenseEvents.ExpenseUpdateEvent;
import pl.financemanagement.Expenses.Model.exceptions.ExpenseNotFoundException;
import pl.financemanagement.Expenses.Model.exceptions.NotEnoughMoneyForTransaction;
import pl.financemanagement.Expenses.Repository.ExpenseRepository;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserAccountRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.Expenses.Model.ExpenseMapper.*;
import static pl.financemanagement.Expenses.Model.ExpenseType.EXPENSE;


@Service("expenseServiceImpl")
public class ExpenseProducerService implements ExpenseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseProducerService.class);

    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ExpenseRepository expenseRepository;
    private final UserAccountRepository userAccountRepository;
    private final BankAccountRepository bankAccountRepository;

    public ExpenseProducerService(KafkaTemplate<String, Object> kafkaTemplate, ExpenseRepository expenseRepository, UserAccountRepository userAccountRepository, BankAccountRepository bankAccountRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.expenseRepository = expenseRepository;
        this.userAccountRepository = userAccountRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, String email) {
        UserAccount userAccount = getUserAccount(email);

        BankAccount bankAccount = bankAccountRepository.findBankAccountById(userAccount.getId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        resolveOperationOnAccountAndCheckBalance(
                expenseRequest.getExpenseType(), bankAccount.getAccountBalance(), expenseRequest.getExpenseCost());

        Expense expense = mapToExpense(expenseRequest);
        expense.setUser(userAccount.getId());
        expense.setVersion(1);

        expenseRepository.save(expense);


        ExpenseCreateEvent expenseCreateEvent = buildExpenseCreateEvent(expenseRequest, bankAccount, userAccount);

        kafkaTemplate.send("expenses_topic", expenseCreateEvent);
        LOGGER.info("Send event {} to kafka", expenseCreateEvent.getExternalId());

        return new ExpenseResponse(mapToDto(expense), true);
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(ExpenseRequest expenseRequest, String email) {
        UserAccount userAccount = getUserAccount(email);

        Expense expense = findExpense(expenseRequest.getExternalId(), userAccount.getId());

        BankAccount bankAccount = findBankAccount(userAccount.getId());

        BigDecimal newBalance = resolveOperationOnAccount(expenseRequest.getExpenseType(), bankAccount, expenseRequest);
        bankAccount.setAccountBalance(newBalance);

        Expense expenseToUpdate = mapToExpense(expenseRequest);
        expenseToUpdate.setModifyOn(Instant.now());

        ExpenseUpdateEvent expenseUpdateEvent = buildExpenseUpdateEvent(expenseRequest, bankAccount, userAccount);

        kafkaTemplate.send("expenses_update_topic", expenseUpdateEvent);
        LOGGER.info("Send event {} to kafka", expenseUpdateEvent.getExternalId());

        return new ExpenseResponse(mapToDtoWithBankBalanceAndUserExternalId(
                expense, bankAccount.getAccountBalance(), UUID.fromString(userAccount.getExternalId())), true);
    }

    @Override
    public List<ExpenseDto> findExpenseByUserName(String email) {
        UserAccount userAccount = getUserAccount(email);
        BankAccount bankAccount = getBankAccountByUserOrThrow(userAccount.getId());

        return expenseRepository.findExpensesByUserId(userAccount.getId())
                .stream()
                .map(expense -> mapToDtoWithBankBalanceAndUserExternalId(
                        expense, bankAccount.getAccountBalance(), UUID.fromString(userAccount.getExternalId())))
                .toList();
    }

    @Override
    public ExpenseResponse findExpenseByIdAndUserId(String expenseExternalId, String email) {
        UserAccount userAccount = getUserAccount(email);
        Expense expense = findExpense(expenseExternalId, userAccount.getId());

        return new ExpenseResponse(mapToDto(expense), true);
    }

    @Override
    public void deleteExpenseByUserExternalIdAndExpenseExternalId(String expenseExternalId, String email) {
        UserAccount userAccount = getUserAccount(email);

        Expense expense = findExpense(expenseExternalId, userAccount.getId());

        kafkaTemplate.send("expenses_delete_topic", new ExpenseDeleteEvent(expense));
        LOGGER.info("Deleted event with id {}, for user {}", expenseExternalId, userAccount.getName());
    }

    private Expense findExpense(String externalId, long id) {
        return expenseRepository.findExpenseByExternalId(externalId, id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID was not found."));
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

    private BankAccount getBankAccountByUserOrThrow(long userId) {
        return bankAccountRepository.findBankAccountById(userId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account for user " + userId + " not found"));
    }

    private UserAccount getUserAccount(String email) {
        return userAccountRepository.findUserByEmail(email)
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

    private BigDecimal resolveOperationOnAccount(ExpenseType expenseType, BankAccount account, ExpenseRequest request) {
        if (expenseType.equals(EXPENSE)) {
            return account.getAccountBalance().subtract(request.getExpenseCost());
        }
        return account.getAccountBalance().add(request.getBankBalance());
    }

    private BankAccount findBankAccount(long userId) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findBankAccountById(userId);
        if (bankAccount.isEmpty()) {
            throw new BankAccountNotFoundException("Account for user " + userId + " not found");
        }
        return bankAccount.get();
    }

    private boolean hasSufficientBalance(BigDecimal bankBalance, BigDecimal expenseAmount) {
        return bankBalance.compareTo(expenseAmount) >= 0;
    }
}
