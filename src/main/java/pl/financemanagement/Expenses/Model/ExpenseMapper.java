package pl.financemanagement.Expenses.Model;

import java.time.Instant;

public class ExpenseMapper {

    public static ExpenseDto mapToDto(Expense expense) {
        return new ExpenseDto.Builder()
                .externalId(expense.getExternalId())
                .expenseCategory(expense.getExpenseCategory())
                .amount(expense.getAmount())
                .userId(expense.getUser().getId())
                .expenseType(expense.getExpenseType())
                .createdOn(expense.getCreatedOn())
                .build();
    }

    public static Expense mapToExpense(ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setExpenseType(request.getExpenseType());
        expense.setExpenseCategory(request.getExpenseCategory());
        expense.setExternalId(request.getExternalId());
        expense.setCreatedOn(Instant.now());
        return expense;
    }

}
