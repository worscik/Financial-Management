package pl.financemanagement.User.UserRepository;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.Optional;

public interface UserDao extends CrudRepository<UserAccount, Long> {

    Optional<UserAccount> findUserByEmail(String email);
    Optional<UserAccount> findUserByEmailAndExternalId(String email, String externalId);

}
