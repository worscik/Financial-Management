package pl.financemanagement.BankAccount.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.financemanagement.BankAccount.Model.BankAccountErrorResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BankAccountHandlerExceptions {

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<BankAccountErrorResponse> handleBankAccountNotFoundException(BankAccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BankAccountErrorResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(BankAccountExistsException.class)
    public ResponseEntity<BankAccountErrorResponse> handleBankAccountExistsException(BankAccountExistsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BankAccountErrorResponse(false, ex.getMessage()));
    }

    private Map<String, String> buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

}
