package pl.financemanagement.JWToken.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JWTokenResponse extends ErrorResponse {

    private String token;
    private String message;
    private String status;

    public JWTokenResponse(String token, String message, String status) {
        super();
        this.token = token;
        this.message = message;
        this.status = status;
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
}
