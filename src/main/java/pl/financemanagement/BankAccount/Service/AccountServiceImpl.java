package pl.financemanagement.BankAccount.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.AccountRequest;
import pl.financemanagement.BankAccount.Model.AccountResponse;

import java.util.UUID;

@Service
@Qualifier("accountServiceImpl")
public class AccountServiceImpl implements AccountService{


    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        return null;
    }

    @Override
    public AccountResponse updateAccount(AccountRequest accountRequest) {
        return null;
    }

    @Override
    public AccountResponse isExistingAccount(String accountNumber) {
        return null;
    }

    @Override
    public AccountResponse getAccountByExternalId(String id) {
        return null;
    }

    @Override
    public boolean deleteAccount(UUID externalId) {
        return false;
    }
}
