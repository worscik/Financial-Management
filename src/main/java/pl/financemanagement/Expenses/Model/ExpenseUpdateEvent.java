package pl.financemanagement.Expenses.Model;

import java.math.BigDecimal;
import java.util.UUID;

public class ExpenseUpdateEvent {

    private UUID externalId;
    private ExpenseCategory expenseCategory;
    private ExpenseType expenseType;
    private BigDecimal bankBalance;
    private BigDecimal expense;
    private long userId;

    public ExpenseUpdateEvent() {
    }

    public ExpenseUpdateEvent(UUID externalId, ExpenseCategory expenseCategory, ExpenseType expenseType, BigDecimal bankBalance, BigDecimal expense, long userId) {
        this.externalId = externalId;
        this.expenseCategory = expenseCategory;
        this.expenseType = expenseType;
        this.bankBalance = bankBalance;
        this.expense = expense;
        this.userId = userId;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
    }
}
