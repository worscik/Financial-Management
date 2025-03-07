package pl.financemanagement.BankAccount.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;

import java.math.BigDecimal;
import java.util.UUID;


@Service
public interface BankAccountService {

    BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email);

    BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email);

    BankAccountResponse findAccountByPrincipal(String email, UUID bankAccountExternalId);

    BankAccountResponse deleteAccount(String email, UUID bankAccountExternalId);

    BigDecimal getBankAccountBalance(String email, UUID bankAccountExternalId);

}
