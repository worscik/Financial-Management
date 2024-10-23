package pl.financemanagement.Expenses.Repository;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseDao extends CrudRepository<Expense, Long> {

    Optional<Expense> findExpenseByExternalIdAndUserId(UUID externalId, long userId);

    List<Expense> findAllExpensesByUserId(long id);

}
