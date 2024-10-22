package pl.financemanagement.BankAccount.Model.Exceptions;

public class BankAccountExistsException extends RuntimeException {

    public BankAccountExistsException(String message) {
        super(message);
    }
}
