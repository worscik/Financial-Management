package pl.financemanagement.User.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserRepository.UserAccountRepository;

@Service
public class UserConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserConsumerService.class);

    private final UserAccountRepository userAccountRepository;

    public UserConsumerService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @KafkaListener(topics = "user_account_delete_topic", groupId = "user-group")
    void consumeUserAccountEvent(UserAccount userAccount) {
        LOGGER.info("Received user account to be delete:" + userAccount.getName());
        userAccountRepository.delete(userAccount);
        LOGGER.info("Removed user account to remove:" + userAccount.getName());
    }

}
