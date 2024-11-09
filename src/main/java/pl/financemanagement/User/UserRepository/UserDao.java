package pl.financemanagement.User.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserCredentialsRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDao {

    private final EntityManager entityManager;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Optional<UserAccount> findUserByCredentials(UserCredentialsRequest credentialsRequest) {
        List<UserAccount> result = entityManager
                .createNativeQuery("SELECT * FROM USER_ACCOUNT WHERE EMAIL = :email AND password = :password", UserAccount.class)
                .setParameter("email", credentialsRequest.getEmail())
                .setParameter("password", credentialsRequest.getPassword())
                .getResultList();
        return result.stream().findFirst();
    }

    @Transactional
    public Optional<UserAccount> findUserByEmail(String email) {
        List<UserAccount> result = entityManager
                .createNativeQuery("SELECT * FROM USER_ACCOUNT WHERE EMAIL = :email", UserAccount.class)
                .setParameter("email", email)
                .getResultList();
        return result.stream().findFirst();
    }

    @Transactional
    public Optional<UserAccount> findUserByEmailAndExternalId(String email, UUID externalId) {
        List<UserAccount> result = entityManager
                .createQuery("SELECT u FROM UserAccount u WHERE u.email = :email AND u.externalId = :externalId", UserAccount.class)
                .setParameter("email", email)
                .setParameter("externalId", externalId)
                .getResultList();
        return result.stream().findFirst();
    }

    @Transactional
    public Optional<UserAccount> findUserById(long id) {
        List<UserAccount> result = entityManager
                .createQuery("SELECT u FROM UserAccount u WHERE u.id = :id", UserAccount.class)
                .setParameter("id", id)
                .getResultList();
        return result.stream().findFirst();
    }

    @Transactional
    public UserAccount saveUserAccount(UserAccount userAccount) {
        return entityManager.merge(userAccount);
    }

    @Transactional
    public void deleteUserAccountById(long id) {
        findUserById(id).ifPresent(entityManager::remove);
    }


}
