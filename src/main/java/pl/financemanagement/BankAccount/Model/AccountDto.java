package pl.financemanagement.BankAccount.Model;

import java.util.UUID;

public class AccountDto {

    private final UUID externalId;
    private final String accountName;
    private final String accountNumber;

    private AccountDto(AccountDtoBuilder builder) {
        this.externalId = builder.externalId;
        this.accountName = builder.accountName;
        this.accountNumber = builder.accountNumber;
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

    public static final class AccountDtoBuilder {
        private UUID externalId;
        private String accountName;
        private String accountNumber;

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

        public AccountDto build() {
            return new AccountDto(this);
        }
    }
}
