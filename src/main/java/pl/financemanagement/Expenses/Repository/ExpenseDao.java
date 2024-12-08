package pl.financemanagement.Expenses.Repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.financemanagement.Expenses.Model.Expense;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExpenseDao {

    private final EntityManager entityManager;

    public ExpenseDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Optional<Expense> findExpenseByExternalIdAndUserId(UUID externalId, long userId) {
        return Optional.empty();
    }

    @Transactional
    public List<Expense> findAllExpensesByUserId(long userId) {
        return Collections.emptyList();
    }

    @Transactional
    public void saveExpense(Expense expense) {
        entityManager.merge(expense);
    }

    @Transactional
    public void deleteExpense(Expense expense){
        entityManager.remove(expense);
    }

}
