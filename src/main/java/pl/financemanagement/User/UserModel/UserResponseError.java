package pl.financemanagement.User.UserModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.ObjectError;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseError extends UserResponse {

    private Map<String, String> errors;


    public UserResponseError(boolean success, UserDto userDto, Map<String, String> errors) {
        super(success, userDto);
        this.errors = errors;
    }

    public UserResponseError(boolean success, Map<String, String> errors) {
        super(success);
        this.errors = errors;
    }

    public Map<String, String> getErrors(boolean success) {
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
