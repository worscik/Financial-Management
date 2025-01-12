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

    private static final String BASIC_EMAIL = "email@email.com";
    private static final String BASIC_NAME = "name";
    private static final String EXTERNAL_ID = "7182075c-8185-4544-a4a6-dff75afeda3f";

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void saveUserAccount() {
        UserAccount userAccount =
                buildUserAccount(BASIC_EMAIL, BASIC_NAME, EXTERNAL_ID);

        userAccountRepository.save(userAccount);

        assertThat(userAccount.getId())
                .isNotNull()
                .isGreaterThan(0);
    }

    @Test
    void findUserByEmail() {
        UserAccount userAccount =
                buildUserAccount(BASIC_EMAIL, BASIC_NAME, EXTERNAL_ID);

        userAccountRepository.save(userAccount);

        Optional<UserAccount> foundUserOpt = userAccountRepository.findUserByEmail(BASIC_EMAIL);

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
                buildUserAccount(BASIC_EMAIL, BASIC_NAME, EXTERNAL_ID);

        userAccountRepository.save(userAccount);

        Optional<UserAccount> expected =
                userAccountRepository.findUserByEmailAndExternalId(BASIC_EMAIL, EXTERNAL_ID);

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findUserWhenDoesNotExist() {
        UserAccount userAccount =
                buildUserAccount(BASIC_EMAIL, BASIC_NAME, EXTERNAL_ID);

        userAccountRepository.save(userAccount);

        Optional<UserAccount> expected =
                userAccountRepository.findUserByEmailAndExternalId("email1@email.com", EXTERNAL_ID);

        assertThat(expected)
                .isEmpty();
    }

    @Test
    void findUserById() {
        UserAccount userAccount =
                buildUserAccount(BASIC_EMAIL, BASIC_NAME, EXTERNAL_ID);

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
                buildUserAccount(BASIC_EMAIL, BASIC_NAME, EXTERNAL_ID);

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