package pl.financemanagement.Expenses.Model.KafkaExpenseEvents;

import pl.financemanagement.Expenses.Model.Expense;

public class ExpenseDeleteEvent {

    private Expense expense;

    public ExpenseDeleteEvent(Expense expense) {
        this.expense = expense;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}

