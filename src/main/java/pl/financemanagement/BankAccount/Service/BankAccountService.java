package pl.financemanagement.BankAccount.Service;

import org.springframework.stereotype.Service;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;


@Service
public interface BankAccountService {

    BankAccountResponse createAccount(BankAccountRequest bankAccountRequest, String email) throws BankAccountExistsException, UserNotFoundException;

    BankAccountResponse updateAccount(BankAccountRequest bankAccountRequest, String email) throws BankAccountNotFoundException, UserNotFoundException;

    BankAccountResponse findAccountByNumber(String accountNumber, String email) throws BankAccountNotFoundException, UserNotFoundException;

    BankAccountResponse deleteAccount(String externalId, String email) throws UserNotFoundException, BankAccountNotFoundException;

}
