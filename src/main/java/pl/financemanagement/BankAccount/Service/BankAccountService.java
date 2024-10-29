package pl.financemanagement.BankAccount.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;

import java.math.BigDecimal;


@Service
public interface BankAccountService {

    BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email);

    BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email);

    BankAccountResponse findAccountByNumber(String accountNumber, String email);

    BankAccountResponse deleteAccount(String email);

    BigDecimal getBankAccountBalance(String email);



}
