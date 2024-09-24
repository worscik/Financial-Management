package pl.financemanagement.BankAccount.Model.Exceptions;

public class BankAccountNotFoundException extends Exception {

    public BankAccountNotFoundException(String message) {
        super(message);
    }

}
