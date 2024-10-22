package pl.financemanagement.BankAccount.Model.Exceptions;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(String message) {
        super(message);
    }

}
