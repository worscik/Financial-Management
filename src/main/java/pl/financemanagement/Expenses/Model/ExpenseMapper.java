package pl.financemanagement.Expenses.Model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpenseMapper {

    public static ExpenseDto mapToDtoWithBankBalanceAndUserExternalId(Expense expense,
                                                                      BigDecimal bankAccount,
                                                                      UUID userAccount) {
        return new ExpenseDto.Builder()
                .externalId(expense.getExternalId().toString())
                .expenseCategory(expense.getExpenseCategory())
                .expenseType(expense.getExpenseType())
                .createdOn(expense.getCreatedOn())
                .bankBalance(bankAccount)
                .userId(userAccount)
                .build();
    }

    public static Expense mapToExpense(ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setExpenseType(request.getExpenseType());
        expense.setExpenseCategory(request.getExpenseCategory());
        expense.setExternalId(UUID.randomUUID().toString());
        expense.setCreatedOn(Instant.now());
        expense.setExpense(request.getExpense());
        return expense;
    }

    public static Expense buildExpenseEntity(ExpenseEvent expenseEvent) {
        Expense expense = new Expense();
        expense.setExpenseType(expenseEvent.getExpenseType());
        expense.setExpenseCategory(expenseEvent.getExpenseCategory());
        expense.setExternalId(expenseEvent.getExternalId().toString());
        expense.setCreatedOn(Instant.now());
        expense.setExpense(expenseEvent.getExpense());
        expense.setUser(expenseEvent.getUserId());
        return expense;
    }

    public static ExpenseDto mapToDto(Expense expense) {
        return new ExpenseDto.Builder()
                .externalId(expense.getExternalId())
                .expenseCategory(expense.getExpenseCategory())
                .expenseType(expense.getExpenseType())
                .createdOn(expense.getCreatedOn())
                .build();
    }

}
