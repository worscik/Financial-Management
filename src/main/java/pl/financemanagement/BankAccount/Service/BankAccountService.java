package pl.financemanagement.BankAccount.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.User.UserModel.UserNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;


@Service
public interface BankAccountService {

    BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email);

    BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email);

    BankAccountResponse findAccountByNumber(String accountNumber, String email);

    BankAccountResponse deleteAccount(String externalId, String email);

    BigDecimal getBankAccountBalance(String email);



}
