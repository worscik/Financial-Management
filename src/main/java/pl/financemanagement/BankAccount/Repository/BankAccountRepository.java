package pl.financemanagement.BankAccount.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.financemanagement.BankAccount.Model.BankAccount;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query(nativeQuery = true, value = """
                        SELECT * 
                        FROM bank_account  
                        WHERE  id = :id
            """)
    Optional<BankAccount> findBankAccountById(@Param("id") long id);

}
