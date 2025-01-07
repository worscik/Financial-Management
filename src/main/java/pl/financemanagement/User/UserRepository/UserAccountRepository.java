package pl.financemanagement.User.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.financemanagement.User.UserModel.UserAccount;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query(nativeQuery = true, value = """
            SELECT * 
            FROM user_account 
            WHERE email = :email
            """)
    Optional<UserAccount> findUserByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM user_account 
            WHERE email = :email 
            AND external_id = :externalId
            """)
    Optional<UserAccount> findUserByEmailAndExternalId(@Param("email") String email,
                                                       @Param("externalId") String externalId);

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM user_account 
            WHERE id = :id
            """)
    Optional<UserAccount> findUserById(@Param("id") long id);

}