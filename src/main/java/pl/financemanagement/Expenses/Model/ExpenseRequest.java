package pl.financemanagement.Expenses.Model;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public class ExpenseRequest {

    private String externalId;
    @NotBlank
    private ExpenseCategory expenseCategory;
    @NotBlank
    private ExpenseType expenseType;
    @NotBlank
    private BigDecimal bankBalance;
    private BigDecimal expenseCost;
    private boolean demo;


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

    public BigDecimal getExpenseCost() {
        return expenseCost;
    }

    public void setExpenseCost(BigDecimal expenseCost) {
        this.expenseCost = expenseCost;
    }
}
