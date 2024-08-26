package pl.financemanagement.BankAccount.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.AccountRequest;
import pl.financemanagement.BankAccount.Model.AccountResponse;

import java.util.UUID;


@Service
public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse updateAccount(AccountRequest accountRequest);

    AccountResponse isExistingAccount(String accountNumber);

    AccountResponse getAccountByExternalId(String id);

    AccountResponse deleteAccount(String externalId);

}
