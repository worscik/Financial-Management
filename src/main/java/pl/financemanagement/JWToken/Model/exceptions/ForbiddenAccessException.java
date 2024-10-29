package pl.financemanagement.JWToken.Model.exceptions;

public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
