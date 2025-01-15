package pl.financemanagement.Expenses.Model.KafkaExpenseEvents;

import pl.financemanagement.Expenses.Model.ExpenseCategory;
import pl.financemanagement.Expenses.Model.ExpenseType;

import java.math.BigDecimal;

public class ExpenseUpdateEvent {

    private String externalId;
    private ExpenseCategory expenseCategory;
    private ExpenseType expenseType;
    private BigDecimal bankBalance;
    private BigDecimal expense;
    private long userId;

    public ExpenseUpdateEvent() {
    }

    public ExpenseUpdateEvent(String externalId, ExpenseCategory expenseCategory, ExpenseType expenseType, BigDecimal bankBalance, BigDecimal expense, long userId) {
        this.externalId = externalId;
        this.expenseCategory = expenseCategory;
        this.expenseType = expenseType;
        this.bankBalance = bankBalance;
        this.expense = expense;
        this.userId = userId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
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
