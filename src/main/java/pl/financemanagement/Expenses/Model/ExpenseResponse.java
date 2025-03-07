package pl.financemanagement.Expenses.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseResponse {

    private final boolean success;
    @JsonProperty("expense")
    private ExpenseDto expenseDto;
    private Map<String, String> errors;

    public ExpenseResponse(ExpenseDto expenseDto, boolean success) {
        this.expenseDto = expenseDto;
        this.success = success;
    }

    public ExpenseResponse(boolean success, ExpenseDto expenseDto, Map<String, String> errors) {
        this.success = success;
        this.expenseDto = expenseDto;
        this.errors = errors;
    }

    public ExpenseResponse(boolean success, Map<String, String> errors) {
        this.success = success;
        this.errors = errors;
    }
}
