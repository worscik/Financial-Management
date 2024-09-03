package pl.financemanagement.BankAccount.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.financemanagement.BankAccount.Model.BankAccount;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

    Optional<BankAccount> findAccountByExternalId(UUID externalId);

}
