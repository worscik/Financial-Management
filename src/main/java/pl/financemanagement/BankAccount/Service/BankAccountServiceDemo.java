package pl.financemanagement.BankAccount.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountDto;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;

import java.util.Map;
import java.util.UUID;

@Service
@Qualifier("bankAccountServiceDemo")
public class BankAccountServiceDemo implements BankAccountService {

    private static final String ACCOUNT_NUMBER = "NL61ABNA4405427607";

    @Override
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest) {
        return new BankAccountResponse(true, buildDemoAccount());
    }

    @Override
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest) {
        return new BankAccountResponse(true, buildDemoAccount());
    }

    @Override
    public BankAccountResponse isExistingAccount(String accountNumber) {
        if(ACCOUNT_NUMBER.equals(accountNumber)){
            return new BankAccountResponse(true, buildDemoAccount());
        }
        return new BankAccountResponse(false, Map.of("Error", "Account not found."));
    }

    @Override
    public BankAccountResponse getAccountByExternalId(String id) {
        return new BankAccountResponse(true, buildDemoAccount());
    }

    @Override
    public BankAccountResponse deleteAccount(String externalId) {
        return new BankAccountResponse(true);
    }

    private BankAccountDto buildDemoAccount() {
        return new BankAccountDto.AccountDtoBuilder()
                .withExternalId(UUID.randomUUID())
                .withAccountName("Test account")
                .withAccountNumber("NL61ABNA4405427607")
                .build();
    }
}
