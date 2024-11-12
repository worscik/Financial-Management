package pl.financemanagement.BankAccount.Model.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;

public class BankAccountNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountNotFoundException.class);

    public BankAccountNotFoundException(String message) {
        super(message);
        LOGGER.info(message);
    }

}
