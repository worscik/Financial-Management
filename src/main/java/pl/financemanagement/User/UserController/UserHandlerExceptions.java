package pl.financemanagement.User.UserController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.financemanagement.User.UserModel.UserErrorResponse;
import pl.financemanagement.User.UserModel.exceptions.UserExistsException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserHandlerExceptions {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserErrorResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Object> handleUserExistsException(UserExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserErrorResponse(false, ex.getMessage()));
    }

    private Map<String, String> buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

}
