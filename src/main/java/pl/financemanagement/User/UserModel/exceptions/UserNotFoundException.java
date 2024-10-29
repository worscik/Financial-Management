package pl.financemanagement.User.UserModel.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotFoundException.class);

    public UserNotFoundException(String message) {
        super(message);
        LOGGER.info(message);
    }
}
