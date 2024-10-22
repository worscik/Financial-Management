package pl.financemanagement.BankAccount.Model;

import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccount mapToAccount(BankAccountRequest bankAccountRequest) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountName(bankAccount.getAccountName());
        bankAccount.setUserId(bankAccount.getUserId());
        bankAccount.setAccountBalance(bankAccountRequest.getAccountBalance());
        return bankAccount;
    }

    public BankAccountDto mapToAccountDto(BankAccount bankAccount) {
        return new BankAccountDto.AccountDtoBuilder()
                .withExternalId(bankAccount.getExternalId())
                .withAccountName(bankAccount.getAccountName())
                .withAccountNumber(bankAccount.getAccountNumber())
                .withAccountBalance(bankAccount.getAccountBalance())
                .build();
    }





}
