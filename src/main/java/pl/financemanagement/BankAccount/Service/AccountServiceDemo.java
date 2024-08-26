package pl.financemanagement.BankAccount.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.AccountDto;
import pl.financemanagement.BankAccount.Model.AccountRequest;
import pl.financemanagement.BankAccount.Model.AccountResponse;

import java.util.Map;
import java.util.UUID;

@Service
@Qualifier("accountServiceDemo")
public class AccountServiceDemo implements AccountService {

    private static final String ACCOUNT_NUMBER = "NL61ABNA4405427607";

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        return new AccountResponse(true, buildDemoAccount());
    }

    @Override
    public AccountResponse updateAccount(AccountRequest accountRequest) {
        return new AccountResponse(true, buildDemoAccount());
    }

    @Override
    public AccountResponse isExistingAccount(String accountNumber) {
        if(ACCOUNT_NUMBER.equals(accountNumber)){
            return new AccountResponse(true, buildDemoAccount());
        }
        return new AccountResponse(false, Map.of("Error", "Account not found."));
    }

    @Override
    public AccountResponse getAccountByExternalId(String id) {
        return new AccountResponse(true, buildDemoAccount());
    }

    @Override
    public AccountResponse deleteAccount(String externalId) {
        return new AccountResponse(true);
    }

    private AccountDto buildDemoAccount() {
        return new AccountDto.AccountDtoBuilder()
                .withExternalId(UUID.randomUUID())
                .withAccountName("Test account")
                .withAccountNumber("NL61ABNA4405427607")
                .build();
    }
}
