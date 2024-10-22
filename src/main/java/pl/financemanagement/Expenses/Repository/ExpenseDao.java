package pl.financemanagement.Expenses.Repository;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.User.UserModel.UserAccount;

public interface ExpenseDao extends CrudRepository<Expense, Long> {

}
