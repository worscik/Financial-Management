package pl.financemanagement.User.UserRepository;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends CrudRepository<UserAccount, Long> {

    Optional<UserAccount> findUserByEmail(String email);
    UserAccount findUserByEmails(String email);
    Optional<UserAccount> findUserByEmailAndExternalId(String email, UUID externalId);

}
