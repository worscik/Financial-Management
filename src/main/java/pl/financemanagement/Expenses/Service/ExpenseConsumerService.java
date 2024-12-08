package pl.financemanagement.Expenses.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseEvent;
import pl.financemanagement.Expenses.Repository.ExpenseDao;
import pl.financemanagement.User.UserRepository.UserDao;

import static pl.financemanagement.Expenses.Model.ExpenseMapper.buildExpenseEntity;

@Service
public class ExpenseConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseConsumerService.class);

    private final ExpenseDao expenseDao;
    private final UserDao userDao;

    public ExpenseConsumerService(ExpenseDao expenseDao, UserDao userDao) {
        this.expenseDao = expenseDao;
        this.userDao = userDao;
    }

    @KafkaListener(topics = "expenses_topic", groupId = "expenses-group")
    public void consumeExpenseEvent(ExpenseEvent expenseEvent) {
        LOGGER.info("Received message from kafka for user: {}", expenseEvent.getUserId());
        Expense expense = buildExpenseEntity(expenseEvent);
        expenseDao.saveExpense(expense);
    }


}
