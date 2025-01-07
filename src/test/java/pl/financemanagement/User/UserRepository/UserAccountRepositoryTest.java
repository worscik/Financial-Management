package pl.financemanagement.User.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void saveUserAccount() {
        UserAccount userAccount =
                buildUserAccount("email@email.com", "name", "7182075c-8185-4544-a4a6-dff75afeda3f");

        userAccountRepository.save(userAccount);

        assertThat(userAccount.getId())
                .isNotNull()
                .isGreaterThan(0);
    }

    @Test
    void findUserByEmail() {
        UserAccount userAccount =
                buildUserAccount("email@email.com", "name", "7182075c-8185-4544-a4a6-dff75afeda3f");

        userAccountRepository.save(userAccount);

        Optional<UserAccount> foundUserOpt = userAccountRepository.findUserByEmail("email@email.com");

        assertThat(foundUserOpt).isPresent();
        UserAccount foundUser = foundUserOpt.get();

        assertThat(foundUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(userAccount);
    }

    @Test
    void findUserByEmailAndExternalId() {
        UserAccount userAccount =
                buildUserAccount("email@email.com", "name", "7182075c-8185-4544-a4a6-dff75afeda3f");

        userAccountRepository.save(userAccount);

        Optional<UserAccount> expected =
                userAccountRepository.findUserByEmailAndExternalId("email@email.com", "7182075c-8185-4544-a4a6-dff75afeda3f");

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findUserWhenDoesNotExist() {
        UserAccount userAccount =
                buildUserAccount("email@email.com", "name", "7182075c-8185-4544-a4a6-dff75afeda3f");

        userAccountRepository.save(userAccount);

        Optional<UserAccount> expected =
                userAccountRepository.findUserByEmailAndExternalId("email1@email.com", "7182075c-8185-4544-a4a6-dff75afeda33");

        assertThat(expected)
                .isEmpty();
    }

    @Test
    void findUserById() {
        UserAccount userAccount =
                buildUserAccount("email@email.com", "name", "7182075c-8185-4544-a4a6-dff75afeda3f");

        UserAccount save = userAccountRepository.save(userAccount);

        Optional<UserAccount> expected = userAccountRepository.findUserById(save.getId());

        assertThat(userAccount.getId())
                .isNotNull()
                .isEqualTo(expected.get().getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {2, 3, 4, 5, 6, 7})
    void findUserByIdWhenUserDoesNotExists(long id) {
        UserAccount userAccount =
                buildUserAccount("email@email.com", "name", "7182075c-8185-4544-a4a6-dff75afeda3f");

        userAccountRepository.save(userAccount);

        Optional<UserAccount> expected = userAccountRepository.findUserById(id);

        assertThat(expected).isEmpty();
    }

    private static UserAccount buildUserAccount(String email, String name, String externalId) {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(email);
        userAccount.setName(name);
        userAccount.setPassword("password");
        userAccount.setRole("USER");
        userAccount.setExternalId(externalId);
        return userAccount;
    }

}