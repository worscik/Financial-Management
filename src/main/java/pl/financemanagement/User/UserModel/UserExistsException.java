package pl.financemanagement.User.UserModel;

public class UserExistsException extends Exception {

    public UserExistsException(String message) {
        super(message);
    }
}
