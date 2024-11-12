package pl.financemanagement.BankAccount.Model;

import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccountDto mapToAccountDto(BankAccount bankAccount) {
        return new BankAccountDto.AccountDtoBuilder()
                .withExternalId(bankAccount.getExternalId())
                .withAccountName(bankAccount.getAccountName())
                .withAccountNumber(bankAccount.getAccountNumber())
                .withAccountBalance(bankAccount.getAccountBalance())
                .build();
    }





}
