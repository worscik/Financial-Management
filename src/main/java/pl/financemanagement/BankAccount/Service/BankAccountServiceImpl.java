package pl.financemanagement.BankAccount.Service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.BankAccountMapper;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountDao;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Repository.ExpenseDao;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.AppTools.AppTools.validUUIDFromString;

@Service
@Qualifier("bankAccountServiceImpl")
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountDao bankAccountDao;
    private final BankAccountMapper bankAccountMapper;
    private final UserDao userDao;
    private final ExpenseDao expenseDao;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao, BankAccountMapper bankAccountMapper, UserDao userDao, ExpenseDao expenseDao) {
        this.bankAccountDao = bankAccountDao;
        this.bankAccountMapper = bankAccountMapper;
        this.userDao = userDao;
        this.expenseDao = expenseDao;
    }

    @Override
    @Transactional
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email) {
        validUUIDFromString(bankAccountRequest.getExternalId());
        UserAccount user = findUserAccount(email);

        BankAccount bankAccount = prepareAccount(bankAccountRequest, user);
        BankAccount savedBankAccount = bankAccountDao.save(bankAccount);
        LOGGER.info("Added account with externalId {} for userId {}",
                savedBankAccount.getExternalId(), savedBankAccount.getUserId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedBankAccount));
    }

    @Override
    @Transactional
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email) {
        validUUIDFromString(bankAccountRequest.getExternalId());
        UserAccount user = findUserAccount(email);
        BankAccount updatedAccount = bankAccountMapper.mapToAccount(bankAccountRequest);

        BankAccount savedAccount = bankAccountDao.save(updatedAccount);
        LOGGER.info("Updated account with externalId {} for userId {}",
                savedAccount.getExternalId(), savedAccount.getUserId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedAccount));
    }

    @Override
    public BankAccountResponse findAccountByNumber(String accountNumber, String email) {
        UserAccount user = findUserAccount(email);
        BankAccount account = findBankAccount(user.getId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(account));
    }

    @Override
    @Transactional
    public BankAccountResponse deleteAccount(String email) {
        UserAccount user = findUserAccount(email);
        BankAccount account = findBankAccount(user.getId());

        List<Expense> expenses = expenseDao.findAllExpensesByUserId(user.getId());
        expenses.forEach(expense -> expenseDao.deleteById(expense.getId()));

        bankAccountDao.deleteById(account.getId());
        userDao.deleteById(user.getId());

        LOGGER.info("Successfully deleted user: {}, bank account: {}, and all expenses",
                user.getEmail(), account.getAccountNumber());
        return new BankAccountResponse(true);
    }

    @Override
    public BigDecimal getBankAccountBalance(String email) {
        UserAccount user = findUserAccount(email);
        BankAccount bankAccount = findBankAccount(user.getId());
        return bankAccount.getAccountBalance();
    }

    private BankAccount prepareAccount(BankAccountRequest bankAccountRequest, UserAccount userAccount) {
        BankAccount bankAccount = bankAccountMapper.mapToAccount(bankAccountRequest);
        bankAccount.setCreatedOn(Instant.now());
        bankAccount.setAccountNumber(UUID.randomUUID().toString());
        bankAccount.setExternalId(UUID.randomUUID());
        bankAccount.setUserId(userAccount.getId());
        return bankAccount;
    }

    private BankAccount findBankAccount(long userId) {
        BankAccount bankAccount = Optional.ofNullable(bankAccountDao.findAccountById(userId))
                .orElseThrow(() -> new BankAccountNotFoundException("Account for user " + userId + " not found"));
        LOGGER.info("Bank account not found for user: {}", userId);
        return bankAccount;
    }

    private UserAccount findUserAccount(String email) {
        UserAccount userAccount = userDao.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        LOGGER.info("User account not found for user: {}", email);
        return userAccount;
    }
}
