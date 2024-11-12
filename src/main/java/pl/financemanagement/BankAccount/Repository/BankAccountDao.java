package pl.financemanagement.BankAccount.Repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.User.UserModel.UserAccount;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BankAccountDao {

    private final EntityManager entityManager;

    public BankAccountDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Optional<BankAccount> findAccountByUserId(long userId) {
        return entityManager
                .createQuery("SELECT ba FROM BankAccount ba WHERE  ba.userID = :userId", BankAccount.class)
                .setParameter("userID", userId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Transactional
    public BankAccount saveBankAccount(BankAccount bankAccount){
       return entityManager.merge(bankAccount);
    }

    @Transactional
    public void deleteBankAccount(BankAccount bankAccount) {
        entityManager.remove(bankAccount);
    }



}
