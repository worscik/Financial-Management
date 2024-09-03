package pl.financemanagement.User.UserRepository;

import org.springframework.data.repository.CrudRepository;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<UserAccount, Long> {

    Optional<UserAccount> findUserByEmail(String email);

    Optional<UserAccount> findUserByExternalId(String externalId);

    Optional<UserAccount> isUserExistByEmail(String email);

}
