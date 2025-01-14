package pl.financemanagement.Expenses.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountRepository;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseCreateEvent;
import pl.financemanagement.Expenses.Model.ExpenseUpdateEvent;
import pl.financemanagement.Expenses.Model.exceptions.ExpenseNotFoundException;
import pl.financemanagement.Expenses.Repository.ExpenseDao;

import java.time.Instant;

@Service
public class ExpenseConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseConsumerService.class);

    private final ExpenseDao expenseDao;
    private final BankAccountRepository bankAccountRepository;

    public ExpenseConsumerService(ExpenseDao expenseDao, BankAccountRepository bankAccountRepository) {
        this.expenseDao = expenseDao;
        this.bankAccountRepository = bankAccountRepository;
    }

    @KafkaListener(topics = "expenses_topic", groupId = "expenses-group")
    public void consumeExpenseEvent(ExpenseCreateEvent event) {
        LOGGER.info("Received create event: {}", event.getExternalId());

        Expense expense = buildExpense(event);
        expenseDao.saveExpense(expense);

        BankAccount bankAccount = bankAccountRepository.findBankAccountById(event.getUserId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found for user " + event.getUserId()));
        bankAccount.setAccountBalance(event.getBankBalance());
        bankAccount.setModifyOn(Instant.now());
        bankAccountRepository.save(bankAccount);
    }

    private static Expense buildExpense(ExpenseCreateEvent event) {
        Expense expense = new Expense();
        expense.setExternalId(event.getExternalId().toString());
        expense.setExpenseCategory(event.getExpenseCategory());
        expense.setExpenseType(event.getExpenseType());
        expense.setExpense(event.getExpense());
        expense.setUser(event.getUserId());
        expense.setCreatedOn(Instant.now());
        return expense;
    }

    @KafkaListener(topics = "expenses_update_topic", groupId = "expenses-group")
    public void consumeExpenseUpdateEvent(ExpenseUpdateEvent event) {
        LOGGER.info("Received update event: {}", event.getExternalId());

        Expense expense = expenseDao.findExpenseByExternalIdAndUserId(event.getExternalId(), event.getUserId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found for update"));

        expense.setExpenseCategory(event.getExpenseCategory());
        expense.setExpenseType(event.getExpenseType());
        expense.setExpense(event.getExpense());
        expense.setModifyOn(Instant.now());
        expenseDao.saveExpense(expense);

        BankAccount bankAccount = bankAccountRepository.findBankAccountById(event.getUserId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found for user " + event.getUserId()));
        bankAccount.setAccountBalance(event.getBankBalance());
        bankAccount.setModifyOn(Instant.now());
        bankAccountRepository.save(bankAccount);
    }

}
