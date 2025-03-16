package pl.financemanagement.BankAccount.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BankAccountRepositoryTest {

    private final static UUID UUID_NUMBER = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2945");

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    void findBankAccountByExistingId() {
        BankAccount account = bankAccountRepository.save(buildBankAccount());

        assertNotNull(account);
        assertThat(account.getId())
                .isNotNull()
                .isGreaterThan(0L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1,2,3,4,5})
    void findBankAccountByExistingId(long id) {
        Optional<BankAccount> account = bankAccountRepository.findBankAccountByUserIdAndExternalId(id, UUID_NUMBER);
        assertThat(account.isEmpty());
    }

    private BankAccount buildBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(UUID_NUMBER);
        bankAccount.setAccountName("TestAccount");
        bankAccount.setExternalId(UUID_NUMBER);
        bankAccount.setUser(new UserAccount());
        return bankAccount;
    }
}