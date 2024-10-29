package pl.financemanagement.User.UserModel.exceptions;

public class UserExistsException extends RuntimeException {

    public UserExistsException(String message) {
        super(message);
    }
}
