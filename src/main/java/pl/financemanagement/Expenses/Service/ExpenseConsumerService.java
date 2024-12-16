package pl.financemanagement.Expenses.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountDao;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseEvents.ExpenseCreateEvent;
import pl.financemanagement.Expenses.Model.ExpenseEvents.ExpenseDeleteEvent;
import pl.financemanagement.Expenses.Model.ExpenseEvents.ExpenseUpdateEvent;
import pl.financemanagement.Expenses.Model.exceptions.ExpenseNotFoundException;
import pl.financemanagement.Expenses.Repository.ExpenseDao;

import java.time.Instant;

@Service
public class ExpenseConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseConsumerService.class);

    private final ExpenseDao expenseDao;
    private final BankAccountDao bankAccountDao;

    public ExpenseConsumerService(ExpenseDao expenseDao, BankAccountDao bankAccountDao) {
        this.expenseDao = expenseDao;
        this.bankAccountDao = bankAccountDao;
    }

    @KafkaListener(topics = "expenses_topic", groupId = "expenses-group")
    public void consumeExpenseEvent(ExpenseCreateEvent event) {
        LOGGER.info("Received create event: {}", event.getExternalId());

        Expense expense = buildExpense(event);
        expenseDao.saveExpense(expense);
        LOGGER.info("Successful save expense on database with id {}", expense.getExternalId());

        BankAccount bankAccount = bankAccountDao.findAccountByUserId(event.getUserId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found for user " + event.getUserId()));
        bankAccount.setAccountBalance(event.getBankBalance());
        bankAccount.setModifyOn(Instant.now());
        bankAccountDao.saveBankAccount(bankAccount);
        LOGGER.info("Saved new account balance for account {}", bankAccount.getExternalId());
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
        LOGGER.info("Successful save expense with id {}", expense.getExternalId());

        BankAccount bankAccount = bankAccountDao.findAccountByUserId(event.getUserId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found for user " + event.getUserId()));
        bankAccount.setAccountBalance(event.getBankBalance());
        bankAccount.setModifyOn(Instant.now());
        bankAccountDao.saveBankAccount(bankAccount);
        LOGGER.info("Saved new balance account for account {}", bankAccount.getExternalId());
    }

    @KafkaListener(topics = "expenses_delete_topic", groupId = "expenses-group")
    public void consumeExpenseEventToDelete(ExpenseDeleteEvent expenseDeleteEvent) {
        Expense expense = expenseDao.findExpenseByExternalIdAndUserId(
                        expenseDeleteEvent.getExternalId(), expenseDeleteEvent.getUserId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID was not found."));

        expenseDao.deleteExpense(expense);
        LOGGER.info("Deleted expense with id {} for user {} ", expense.getExternalId(), expense.getUser());
    }

    private static Expense buildExpense(ExpenseCreateEvent event) {
        Expense expense = new Expense();
        expense.setExternalId(event.getExternalId());
        expense.setExpenseCategory(event.getExpenseCategory());
        expense.setExpenseType(event.getExpenseType());
        expense.setExpense(event.getExpense());
        expense.setUser(event.getUserId());
        expense.setCreatedOn(Instant.now());
        return expense;
    }

}
