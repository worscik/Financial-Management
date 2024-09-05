package pl.financemanagement.User.UserModel;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;


public class UserErrorResponse extends UserResponse {

    private Map<String, String> errors;
    private String error;

    public UserErrorResponse(boolean success, Map<String, String> errors) {
        super(success);
        this.errors = errors;
    }

    public UserErrorResponse(boolean success, String error) {
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
