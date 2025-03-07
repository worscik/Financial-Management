package pl.financemanagement.BankAccount.Model;

import java.math.BigDecimal;
import java.util.UUID;

public class BankAccountDto {

    private final UUID externalId;
    private final String accountName;
    private final UUID accountNumber;
    private final BigDecimal accountBalance;
    private final Currency currency;

    private BankAccountDto(AccountDtoBuilder builder) {
        this.externalId = builder.externalId;
        this.accountName = builder.accountName;
        this.accountNumber = builder.accountNumber;
        this.accountBalance = builder.accountBalance;
        this.currency = builder.currency;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public String getAccountName() {
        return accountName;
    }

    public UUID getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public static final class AccountDtoBuilder {
        private UUID externalId;
        private String accountName;
        private UUID accountNumber;
        private BigDecimal accountBalance;
        private Currency currency;

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

        public AccountDtoBuilder withAccountNumber(UUID accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountDtoBuilder withAccountBalance(BigDecimal accountBalance) {
            this.accountBalance = accountBalance;
            return this;
        }

        public AccountDtoBuilder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public BankAccountDto build() {
            return new BankAccountDto(this);
        }
    }
}
