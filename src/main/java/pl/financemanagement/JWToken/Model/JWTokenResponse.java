package pl.financemanagement.JWToken.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.financemanagement.User.UserModel.UserRole;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JWTokenResponse extends ErrorResponse {

    private String token;
    private String message;
    private String status;
    private UserRole userRole;

    public JWTokenResponse(String token, String message, String status, UserRole userRole) {
        super();
        this.token = token;
        this.message = message;
        this.status = status;
        this.userRole = userRole;
    }

    public JWTokenResponse(Map<String, String> errors, String status) {
        super(errors);
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
