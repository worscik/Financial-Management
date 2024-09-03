package pl.financemanagement.Expenses;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseResponse;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {

}
