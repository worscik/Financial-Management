package pl.financemanagement.Expenses.Model.exceptions;

public class NotEnoughMoneyForTransaction extends RuntimeException {

    public NotEnoughMoneyForTransaction(String message) {
        super(message);
    }
}
