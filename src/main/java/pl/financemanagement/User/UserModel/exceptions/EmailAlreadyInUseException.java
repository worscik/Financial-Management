package pl.financemanagement.User.UserModel.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("User with email " + email + " already exists.");
    }

}
