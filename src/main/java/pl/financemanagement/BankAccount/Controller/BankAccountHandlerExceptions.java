package pl.financemanagement.BankAccount.Controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.financemanagement.BankAccount.Model.BankAccountErrorResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;

@ControllerAdvice
public class BankAccountHandlerExceptions {

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<Object> handleBankAccountNotFoundException(BankAccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BankAccountErrorResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(BankAccountExistsException.class)
    public ResponseEntity<Object> handleBankAccountExistsException(BankAccountExistsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BankAccountErrorResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BankAccountErrorResponse(false, ex.getMessage()));
    }

}
