package pl.financemanagement.Expenses.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.financemanagement.BankAccount.Model.BankAccountErrorResponse;
import pl.financemanagement.Expenses.Model.exceptions.NotEnoughMoneyForTransaction;

@ControllerAdvice
public class ExpenseHandlerExceptions {

    @ExceptionHandler(NotEnoughMoneyForTransaction.class)
    public ResponseEntity<BankAccountErrorResponse> handleBankAccountNotFoundException(NotEnoughMoneyForTransaction ex) {
        return ResponseEntity.ok().body(
                new BankAccountErrorResponse(false, "There are not enough funds in the account for the account"));
    }

}
