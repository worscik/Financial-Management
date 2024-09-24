package pl.financemanagement.BankAccount.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.BankAccountMapper;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountDao;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;
import pl.financemanagement.User.UserService.UserServiceImpl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.AppTools.AppTools.validUUIDFromString;

@Service
@Qualifier("bankAccountServiceImpl")
public class BankAccountServiceImpl implements BankAccountService {

    private static final long ONE = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final BankAccountDao bankAccountDao;
    private final BankAccountMapper bankAccountMapper;
    private final UserDao userDao;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao, BankAccountMapper bankAccountMapper, UserDao userDao) {
        this.bankAccountDao = bankAccountDao;
        this.bankAccountMapper = bankAccountMapper;
        this.userDao = userDao;
    }

    @Override
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email)
            throws BankAccountExistsException, UserNotFoundException {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        if (user.isEmpty()) {
            LOGGER.info("User with email not found: {}", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        UUID externalId = validUUIDFromString(bankAccountRequest.getExternalId());
        Optional<BankAccount> existingAccount = bankAccountDao.findAccountByExternalIdAndUserId(
                externalId, user.get().getId());
        if (existingAccount.isPresent()) {
            LOGGER.info("Account with ID {} already exists", bankAccountRequest.getExternalId());
            throw new BankAccountExistsException("Account with externalId" + externalId + " is exists");
        }

        BankAccount bankAccount = prepareAccount(bankAccountRequest);
        BankAccount savedBankAccount = bankAccountDao.save(bankAccount);
        LOGGER.info("Added account with externalId {} for userId {}",
                savedBankAccount.getExternalId(), savedBankAccount.getUserId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedBankAccount));
    }

    @Override
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email)
            throws BankAccountNotFoundException, UserNotFoundException {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        if (user.isEmpty()) {
            LOGGER.info("User with email not found: {}", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        UUID externalId = validUUIDFromString(bankAccountRequest.getExternalId());
        Optional<BankAccount> accountOptional =
                bankAccountDao.findAccountByExternalIdAndUserId(externalId, user.get().getId());
        if (accountOptional.isEmpty()) {
            LOGGER.info("Account with externalId {} not found", bankAccountRequest.getExternalId());
            throw new BankAccountNotFoundException("Account not found");
        }
        BankAccount existingAccount = accountOptional.get();
        BankAccount updatedAccount = bankAccountMapper.mapToAccount(bankAccountRequest);
        updatedAccount.setId(existingAccount.getId());

        BankAccount savedAccount = bankAccountDao.save(updatedAccount);
        LOGGER.info("Updated account with externalId {} for userId {}",
                savedAccount.getExternalId(), savedAccount.getUserId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedAccount));
    }

    @Override
    public BankAccountResponse findAccountByNumber(String accountNumber, String email)
            throws BankAccountNotFoundException, UserNotFoundException {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        if (user.isEmpty()) {
            LOGGER.info("User with email not found: {}", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        Optional<BankAccount> account =
                bankAccountDao.findAccountByExternalIdAndUserId(UUID.fromString(accountNumber), user.get().getId());
        if (account.isPresent()) {
            return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(account.get()));
        }
        LOGGER.info("Bank account not found for user: {}", email);
        throw new BankAccountNotFoundException("Account not found");
    }

    @Override
    public BankAccountResponse deleteAccount(String externalId, String email)
            throws UserNotFoundException, BankAccountNotFoundException {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        if (user.isEmpty()) {
            LOGGER.info("User with email not found: {}", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        Optional<BankAccount> account =
                bankAccountDao.findAccountByExternalIdAndUserId(UUID.fromString(externalId), user.get().getId());
        if (account.isPresent()) {
            userDao.deleteById(account.get().getId());
            return new BankAccountResponse(true);
        }
        throw new BankAccountNotFoundException("Account with ID " + externalId + " not found");
    }

    private BankAccount prepareAccount(BankAccountRequest bankAccountRequest) {
        BankAccount bankAccount = bankAccountMapper.mapToAccount(bankAccountRequest);
        bankAccount.setAccountVersion(ONE);
        bankAccount.setCreatedOn(Instant.now());
        bankAccount.setAccountNumber(UUID.randomUUID().toString());
        bankAccount.setExternalId(UUID.randomUUID());
        return bankAccount;
    }
}
