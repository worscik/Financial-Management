package pl.financemanagement.BankAccount.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.financemanagement.BankAccount.Model.BankAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query(nativeQuery = true, value = """
                        SELECT * 
                        FROM bank_account  
                        WHERE  user_id = :user_id and external_id = :external_id
            """)
    Optional<BankAccount> findBankAccountByUserIdAndExternalId(@Param("user_id") long user_id,
                                                               @Param("external_id") UUID external_id);

    @Query(nativeQuery = true, value = """
                        select currency 
                        from bank_account 
                        where user_id = :userId
            """)
    List<String> findAllAccountCurrenciesForUser(@Param("userId") long userId);
}
