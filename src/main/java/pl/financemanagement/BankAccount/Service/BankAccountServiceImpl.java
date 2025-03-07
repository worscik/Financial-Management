package pl.financemanagement.BankAccount.Service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.*;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Repository.BankAccountRepository;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Repository.ExpenseRepository;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserAccountRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Qualifier("bankAccountServiceImpl")
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);


    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final UserAccountRepository userAccountRepository;
    private final ExpenseRepository expenseRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  BankAccountMapper bankAccountMapper,
                                  UserAccountRepository userAccountRepository,
                                  ExpenseRepository expenseRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.userAccountRepository = userAccountRepository;
        this.expenseRepository = expenseRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email) {

        UserAccount user = getUserByEmailOrThrow(email);

        if (checkIfUserCanCreateBankAccount(user.getId(), bankAccountRequest.getCurrency())) {
            throw new BankAccountExistsException("Account with currency " + bankAccountRequest.getCurrency() + " already exists");
        }

        BankAccount accountToSave = prepareAccount(bankAccountRequest, user);
        BankAccount savedBankAccount = bankAccountRepository.save(accountToSave);

        LOGGER.info("Added account with externalId {} for userId {}",
                savedBankAccount.getExternalId(), savedBankAccount.getUser().getId());

        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedBankAccount));
    }

    @Override
    @Transactional
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email) {
//        validUUIDFromString(bankAccountRequest.getExternalId());

        UserAccount user = getUserByEmailOrThrow(email);
        BankAccount bankAccount = getBankAccountByUserOrThrow(user.getId(), bankAccountRequest.getExternalId());

        BankAccount updatedAccount = prepareAccountToUpdate(bankAccountRequest, bankAccount);

        BankAccount savedAccount = bankAccountRepository.save(updatedAccount);

        LOGGER.info("Updated account with externalId {} for userId {}",
                savedAccount.getExternalId(), savedAccount.getUser().getId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedAccount));
    }

    @Override
    public BankAccountResponse findAccountByPrincipal(String email, UUID externalId) {
        UserAccount user = getUserByEmailOrThrow(email);
        BankAccount account = getBankAccountByUserOrThrow(user.getId(), externalId);

        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(account));
    }

    @Override
    @Transactional
    public BankAccountResponse deleteAccount(String email, UUID bankAccountExternalId) {
        UserAccount user = getUserByEmailOrThrow(email);
        BankAccount bankAccount = getBankAccountByUserOrThrow(user.getId(), bankAccountExternalId);

        List<Expense> expenses = expenseRepository.findExpensesByUserId(user.getId());
        expenses.forEach(expense -> kafkaTemplate.send("expenses_delete_topic", expense));

        kafkaTemplate.send("bank_account_delete_topic", bankAccount);
        kafkaTemplate.send("user_account_delete_topic", user);

        LOGGER.info("Successfully deleted user: {}, bank account: {}, and all expenses",
                user.getEmail(), bankAccount.getAccountNumber());
        return new BankAccountResponse(true);
    }

    @Override
    public BigDecimal getBankAccountBalance(String email, UUID bankAccountExternalId) {
        UserAccount user = getUserByEmailOrThrow(email);
        BankAccount account = getBankAccountByUserOrThrow(user.getId(), bankAccountExternalId);
        return account.getAccountBalance();
    }

    public boolean checkIfUserCanCreateBankAccount(long userId, Currency currency) {
        return bankAccountRepository.findAllAccountCurrenciesForUser(userId)
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(accountCurrency -> accountCurrency.equals(currency.name()));
    }

    private BankAccount prepareAccount(BankAccountRequest bankAccountRequest, UserAccount userAccount) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(userAccount);
        bankAccount.setAccountName(bankAccountRequest.getAccountName());
        bankAccount.setAccountBalance(bankAccountRequest.getAccountBalance());
        bankAccount.setCreatedOn(Instant.now());
        bankAccount.setAccountNumber(UUID.randomUUID());
        bankAccount.setExternalId(UUID.randomUUID());
        bankAccount.setCurrency(bankAccountRequest.getCurrency());
        return bankAccount;
    }

    private BankAccount prepareAccountToUpdate(BankAccountRequest bankAccountRequest,
                                               BankAccount bankAccount) {
        bankAccount.setAccountName(bankAccountRequest.getAccountName());
        bankAccount.setAccountBalance(bankAccountRequest.getAccountBalance());
        bankAccount.setModifyOn(Instant.now());
        return bankAccount;
    }

    private UserAccount getUserByEmailOrThrow(String email) {
        return userAccountRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    private BankAccount getBankAccountByUserOrThrow(long userId, UUID externalId) {
        return bankAccountRepository.findBankAccountByUserIdAndExternalId(userId, externalId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account for user " + userId + " not found"));
    }

}
