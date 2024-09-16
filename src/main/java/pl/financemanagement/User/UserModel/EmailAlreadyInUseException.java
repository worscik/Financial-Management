package pl.financemanagement.User.UserModel;

public class EmailAlreadyInUseException extends Exception {

    public EmailAlreadyInUseException(String email) {
        super("User with email " + email + " already exists.");
    }

}
