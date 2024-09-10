package pl.financemanagement.ApplicationConfig.JWToken;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JWTokenResponse {

    private String token;
    private String message;
    private String status;

    public JWTokenResponse(String token, String message, String status) {
        this.token = token;
        this.message = message;
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
