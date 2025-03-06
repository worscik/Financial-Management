package pl.financemanagement.JWToken.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.financemanagement.User.UserModel.UserRole;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
public class JWTokenResponse extends ErrorResponse {

    private String token;
    private String message;
    private boolean status;
    private UserRole userRole;

    public JWTokenResponse(String token, String message, boolean status, UserRole userRole) {
        super();
        this.token = token;
        this.message = message;
        this.status = status;
        this.userRole = userRole;
    }

    public JWTokenResponse(Map<String, String> errors, boolean status) {
        super(errors);
        this.status = status;
    }

}
