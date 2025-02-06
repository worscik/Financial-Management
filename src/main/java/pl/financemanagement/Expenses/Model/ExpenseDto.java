package pl.financemanagement.Expenses.Model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpenseDto {

    private final String externalId;
    private final Instant createdOn;
    private final ExpenseCategory expenseCategory;
    private final ExpenseType expenseType;
    private final String userExternalId;
    private final BigDecimal bankBalance;

    private ExpenseDto(Builder builder) {
        this.externalId = builder.externalId;
        this.createdOn = builder.createdOn;
        this.expenseCategory = builder.expenseCategory;
        this.expenseType = builder.expenseType;
        this.userExternalId = builder.userExternalId;
        this.bankBalance = builder.bankBalance;
    }

    public static class Builder {
        private String externalId;
        private Instant createdOn;
        private ExpenseCategory expenseCategory;
        private ExpenseType expenseType;
        private String userExternalId;
        private BigDecimal bankBalance;

        public Builder externalId(String externalId) {
            this.externalId = externalId;
            return this;
        }

        public Builder createdOn(Instant createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder expenseCategory(ExpenseCategory expenseCategory) {
            this.expenseCategory = expenseCategory;
            return this;
        }

        public Builder expenseType(ExpenseType expenseType) {
            this.expenseType = expenseType;
            return this;
        }

        public Builder userId(String userExternalId) {
            this.userExternalId = userExternalId;
            return this;
        }

        public Builder bankBalance(BigDecimal bankBalance) {
            this.bankBalance = bankBalance;
            return this;
        }

        public ExpenseDto build() {
            return new ExpenseDto(this);
        }
    }

    public String getExternalId() {
        return externalId;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public String getUserExternalId() {
        return userExternalId;
    }

    public BigDecimal getBankBalance() {
        return bankBalance;
    }


}
