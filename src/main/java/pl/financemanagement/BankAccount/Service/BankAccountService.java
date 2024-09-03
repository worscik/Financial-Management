package pl.financemanagement.BankAccount.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;


@Service
public interface BankAccountService {

    BankAccountResponse createAccount(BankAccountRequest bankAccountRequest);

    BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest);

    BankAccountResponse isExistingAccount(String accountNumber);

    BankAccountResponse getAccountByExternalId(String id);

    BankAccountResponse deleteAccount(String externalId);

}
