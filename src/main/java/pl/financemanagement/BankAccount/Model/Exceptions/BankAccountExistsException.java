package pl.financemanagement.BankAccount.Model.Exceptions;

public class BankAccountExistsException extends Exception {

    public BankAccountExistsException(String message) {
        super(message);
    }
}
