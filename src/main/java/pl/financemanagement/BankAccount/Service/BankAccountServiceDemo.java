package pl.financemanagement.BankAccount.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountDto;
import pl.financemanagement.BankAccount.Model.BankAccountErrorResponse;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@Qualifier("bankAccountServiceDemo")
public class BankAccountServiceDemo implements BankAccountService {

    private static final String ACCOUNT_NUMBER = "NL61ABNA4405427607";

    @Override
    public BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email) {
        return new BankAccountResponse(true, buildDemoAccount());
    }

    @Override
    public BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email) {
        return new BankAccountResponse(true, buildDemoAccount());
    }

    @Override
    public BankAccountResponse findAccountByPrincipal(String email) {
            return new BankAccountResponse(true, buildDemoAccount());
    }

    @Override
    public BankAccountResponse deleteAccount( String email) {
        return new BankAccountResponse(true);
    }

    @Override
    public BigDecimal getBankAccountBalance(String email) {
        return BigDecimal.valueOf(10_000);
    }

    private BankAccountDto buildDemoAccount() {
        return new BankAccountDto.AccountDtoBuilder()
                .withExternalId(UUID.randomUUID())
                .withAccountName("Test account")
                .withAccountNumber(ACCOUNT_NUMBER)
                .build();
    }
}
