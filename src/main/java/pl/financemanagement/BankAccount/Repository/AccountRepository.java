package pl.financemanagement.BankAccount.Repository;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.BankAccount.Model.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
