package pl.financemanagement.Expenses.Model;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public class ExpenseRequest {

    private UUID externalId;
    @NotBlank
    private ExpenseCategory expenseCategory;
    @NotBlank
    private ExpenseType expenseType;
    @NotBlank
    private BigDecimal bankBalance;
    private BigDecimal expense;
    private boolean demo;


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

    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
    }

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
