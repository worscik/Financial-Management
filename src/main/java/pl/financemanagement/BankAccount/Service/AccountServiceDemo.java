package pl.financemanagement.BankAccount.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import pl.financemanagement.BankAccount.Model.AccountRequest;
import pl.financemanagement.BankAccount.Model.AccountResponse;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserService.UserService;

import java.util.UUID;

@Qualifier("accountServiceDemo")
public class AccountServiceDemo implements AccountService {

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        return null;
    }

    @Override
    public AccountResponse updateAccount(AccountRequest accountRequest) {
        return null;
    }

    @Override
    public AccountResponse isExistingAccount(UUID accountNumber) {
        return null;
    }

    @Override
    public AccountResponse getAccountByExternalId(long id) {
        return null;
    }

    @Override
    public boolean deleteAccount(UUID externalId) {
        return false;
    }
}
