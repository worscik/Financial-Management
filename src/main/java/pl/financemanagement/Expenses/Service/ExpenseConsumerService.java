package pl.financemanagement.Expenses.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountRepository;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.KafkaExpenseEvents.ExpenseCreateEvent;
import pl.financemanagement.Expenses.Model.KafkaExpenseEvents.ExpenseDeleteEvent;
import pl.financemanagement.Expenses.Model.KafkaExpenseEvents.ExpenseUpdateEvent;
import pl.financemanagement.Expenses.Model.exceptions.ExpenseNotFoundException;
import pl.financemanagement.Expenses.Repository.ExpenseRepository;

import java.time.Instant;

@Service
public class ExpenseConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseConsumerService.class);

    private final ExpenseRepository expenseRepository;
    private final BankAccountRepository bankAccountRepository;

    public ExpenseConsumerService(ExpenseRepository expenseRepository, BankAccountRepository bankAccountRepository) {
        this.expenseRepository = expenseRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @KafkaListener(topics = "expenses_topic", groupId = "expenses-group")
    void consumeExpenseEvent(ExpenseCreateEvent event) {
        LOGGER.info("Received create event: {}", event.getExternalId());

        Expense expense = buildExpense(event);
        expenseRepository.save(expense);

        BankAccount bankAccount = bankAccountRepository.findBankAccountById(event.getUserId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found for user " + event.getUserId()));
        bankAccount.setAccountBalance(event.getBankBalance());
        bankAccount.setModifyOn(Instant.now());
        bankAccountRepository.save(bankAccount);
    }

    @KafkaListener(topics = "expenses_update_topic", groupId = "expenses-group")
    void consumeExpenseUpdateEvent(ExpenseUpdateEvent event) {
        LOGGER.info("Received update event: {}", event.getExternalId());

        Expense expense = expenseRepository.findExpenseByExternalIdAndUserId(event.getExternalId(), event.getUserId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found for update"));

        expense.setExpenseCategory(event.getExpenseCategory());
        expense.setExpenseType(event.getExpenseType());
        expense.setExpense(event.getExpense());
        expense.setModifyOn(Instant.now());
        expenseRepository.save(expense);

        BankAccount bankAccount = bankAccountRepository.findBankAccountById(event.getUserId())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found for user " + event.getUserId()));
        bankAccount.setAccountBalance(event.getBankBalance());
        bankAccount.setModifyOn(Instant.now());
        bankAccountRepository.save(bankAccount);
    }

    @KafkaListener(topics = "expenses_delete_topic", groupId = "expenses-group")
    void deleteExpenseEvent(ExpenseDeleteEvent expenseEvent) {
        LOGGER.info("Received event to delete: {}", expenseEvent.getExpense().getExternalId());

        expenseRepository.delete(expenseEvent.getExpense());
        LOGGER.info("Deleted expense with externalId: {}", expenseEvent.getExpense().getExternalId());
    }

    private static Expense buildExpense(ExpenseCreateEvent event) {
        Expense expense = new Expense();
        expense.setExternalId(event.getExternalId());
        expense.setExpenseCategory(event.getExpenseCategory());
        expense.setExpenseType(event.getExpenseType());
        expense.setExpense(event.getExpense());
        expense.setUserId(event.getUserId());
        expense.setCreatedOn(Instant.now());
        return expense;
    }

}
