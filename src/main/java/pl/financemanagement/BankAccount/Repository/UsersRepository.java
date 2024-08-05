package pl.financemanagement.BankAccount.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class UsersRepository implements CrudRepository<UsersRepository, Long> {
}
