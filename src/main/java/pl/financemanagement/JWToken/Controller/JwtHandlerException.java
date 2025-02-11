package pl.financemanagement.JWToken.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.financemanagement.JWToken.Model.JWTokenResponse;
import pl.financemanagement.JWToken.Model.exceptions.ForbiddenAccessException;

import static pl.financemanagement.JWToken.Model.JWTokenStatus.ERROR;

@ControllerAdvice
public class JwtHandlerException {

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<JWTokenResponse> handleForbiddenAccessException(ForbiddenAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JWTokenResponse(null,
                "Incorrect login information sent", ERROR.getStatus(), null));
    }

}
