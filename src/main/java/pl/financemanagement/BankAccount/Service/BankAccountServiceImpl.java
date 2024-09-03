package pl.financemanagement.BankAccount.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Model.BankAccountMapper;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Repository.BankAccountRepository;
import pl.financemanagement.User.UserService.UserServiceImpl;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("bankAccountServiceImpl")
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest) {
        if (!Objects.isNull(bankAccountRequest.getExternalId())) {
            Optional<BankAccount> existingAccount = bankAccountRepository.findAccountByExternalId(
                    UUID.fromString(bankAccountRequest.getExternalId()));
            if (existingAccount.isPresent()) {
                log.info("Account with ID {} exists", bankAccountRequest.getExternalId());
                return new BankAccountResponse(false, Map.of("account", "Account with id "
                        + existingAccount.get().getExternalId() + " exists"));
            }
        }
        BankAccount bankAccount = prepareAccount(bankAccountRequest);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        log.info("Add account with externalId {} and userId {}", bankAccount.getExternalId(), bankAccount.getUserId());
        return new BankAccountResponse(true, bankAccountMapper.mapToAccountDto(savedBankAccount));
    }

    @Override
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest) {
        return null;
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
        bankAccount.setAccountVersion(1L);
        bankAccount.setCreatedOn(Instant.now());
        bankAccount.setAccountNumber(UUID.randomUUID().toString());
        bankAccount.setExternalId(UUID.randomUUID());
        return bankAccount;
    }
}
