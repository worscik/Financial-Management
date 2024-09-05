package pl.financemanagement.BankAccount.Model;

import java.util.Map;

public class BankAccountErrorResponse extends BankAccountResponse {

    private Map<String, String> errors;
    private String error;

    public BankAccountErrorResponse(boolean success, Map<String, String> errors) {
        super(success);
        this.errors = errors;
    }

    public BankAccountErrorResponse(boolean success, String error) {
        super(success);
        this.error = error;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
