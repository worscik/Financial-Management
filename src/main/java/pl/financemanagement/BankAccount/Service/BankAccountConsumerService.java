package pl.financemanagement.BankAccount.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import pl.financemanagement.BankAccount.Model.BankAccount;
import pl.financemanagement.BankAccount.Repository.BankAccountRepository;
import pl.financemanagement.User.UserModel.UserAccount;

public class BankAccountConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountConsumerService.class);

    private final BankAccountRepository bankAccountRepository;

    public BankAccountConsumerService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @KafkaListener(topics = "back_account_delete_topic", groupId = "bank-account-group")
    void consumeBankAccountEvent(BankAccount bankAccount) {
        LOGGER.info("Received user account to be delete:" + bankAccount.getAccountName());
        bankAccountRepository.delete(bankAccount);
        LOGGER.info("Removed user account to remove:" + bankAccount.getAccountName());
    }
}
