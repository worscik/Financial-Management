package pl.financemanagement.BankAccount.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.*;
import pl.financemanagement.BankAccount.Repository.BankAccountDao;
import pl.financemanagement.User.UserService.UserServiceImpl;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("bankAccountServiceImpl")
public class BankAccountServiceImpl implements BankAccountService {

    private static final long ONE = 1;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final BankAccountDao bankAccountDao;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao, BankAccountMapper bankAccountMapper) {
        this.bankAccountDao = bankAccountDao;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest) {
        if (Objects.nonNull(bankAccountRequest.getExternalId())) {
            UUID externalId;
            try {
                externalId = UUID.fromString(bankAccountRequest.getExternalId());
            } catch (IllegalArgumentException e) {
                log.error("Invalid UUID format for externalId: {}", bankAccountRequest.getExternalId());
                throw new IllegalArgumentException("Invalid UUID format for externalId");
            } 

            Optional<BankAccount> existingAccount = bankAccountDao.findAccountByExternalId(externalId);
            if (existingAccount.isPresent()) {
                log.info("Account with ID {} already exists", bankAccountRequest.getExternalId());
                return new BankAccountErrorResponse(false, "Account with id " + externalId + " already exists");
            }
        }

        BankAccount bankAccount = prepareAccount(bankAccountRequest);
        BankAccount savedBankAccount = bankAccountDao.save(bankAccount);
        log.info("Added account with externalId {} for userId {}", savedBankAccount.getExternalId(), savedBankAccount.getUserId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedBankAccount));
    }

    @Override
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest) {
        UUID externalId;
        try {
            externalId = UUID.fromString(bankAccountRequest.getExternalId());
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format for externalId: {}", bankAccountRequest.getExternalId());
            throw new IllegalArgumentException("Invalid UUID format for externalId");
        }

        Optional<BankAccount> accountOptional = bankAccountDao.findAccountByExternalId(externalId);
        if (accountOptional.isPresent()) {
            BankAccount existingAccount = accountOptional.get();
            BankAccount updatedAccount = bankAccountMapper.mapToAccount(bankAccountRequest);
            updatedAccount.setId(existingAccount.getId());

            BankAccount savedAccount = bankAccountDao.save(updatedAccount);
            log.info("Updated account with externalId {} for userId {}", savedAccount.getExternalId(), savedAccount.getUserId());
            return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedAccount));
        }

        log.info("Account with externalId {} not found", bankAccountRequest.getExternalId());
        return new BankAccountErrorResponse(false, "Bank account not found");
    }

    @Override
    public BankAccountResponse isExistingAccount(String accountNumber) {
        return null;
    }

    @Override
    public BankAccountResponse getAccountByExternalId(String id) {
        return null;
    }

    @Override
    public BankAccountResponse deleteAccount(String externalId) {
        return new BankAccountResponse(true);
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
