package pl.financemanagement.BankAccount.Model;

import java.math.BigDecimal;
import java.util.UUID;

public class BankAccountDto {

    private final UUID externalId;
    private final String accountName;
    private final String accountNumber;
    private final BigDecimal accountBalance;

    private BankAccountDto(AccountDtoBuilder builder) {
        this.externalId = builder.externalId;
        this.accountName = builder.accountName;
        this.accountNumber = builder.accountNumber;
        this.accountBalance = builder.accountBalance;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public static final class AccountDtoBuilder {
        private UUID externalId;
        private String accountName;
        private String accountNumber;
        private BigDecimal accountBalance;

        public AccountDtoBuilder() {
        }

        public static AccountDtoBuilder anAccountDto() {
            return new AccountDtoBuilder();
        }

        public AccountDtoBuilder withExternalId(UUID externalId) {
            this.externalId = externalId;
            return this;
        }

        public AccountDtoBuilder withAccountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public AccountDtoBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountDtoBuilder withAccountBalance(BigDecimal accountBalance) {
            this.accountBalance = accountBalance;
            return this;
        }

        public BankAccountDto build() {
            return new BankAccountDto(this);
        }
    }
}
