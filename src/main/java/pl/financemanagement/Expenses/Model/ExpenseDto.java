package pl.financemanagement.Expenses.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpenseDto {

    private final UUID externalId;
    private final Instant createdOn;
    private final ExpenseCategory expenseCategory;
    private final ExpenseType expenseType;
    private final long userId;
    private final BigDecimal amount;

    private ExpenseDto(Builder builder) {
        this.externalId = builder.externalId;
        this.createdOn = builder.createdOn;
        this.expenseCategory = builder.expenseCategory;
        this.expenseType = builder.expenseType;
        this.userId = builder.userId;
        this.amount = builder.amount;
    }

    public static class Builder {
        private UUID externalId;
        private Instant createdOn;
        private ExpenseCategory expenseCategory;
        private ExpenseType expenseType;
        private long userId;
        private BigDecimal amount;

        public Builder externalId(UUID externalId) {
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

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public ExpenseDto build() {
            return new ExpenseDto(this);
        }
    }

    public UUID getExternalId() {
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

    public long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }


}
