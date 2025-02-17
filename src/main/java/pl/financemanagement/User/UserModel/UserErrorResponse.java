package pl.financemanagement.User.UserModel;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
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

}
