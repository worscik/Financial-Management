package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private boolean success;
    @JsonProperty("account")
    private AccountDto userDto;
    private Map<String,String> errors;

    public AccountResponse(boolean success, AccountDto userDto, Map<String,String> errors) {
        this.success = success;
        this.userDto = userDto;
        this.errors = errors;
    }

    public AccountResponse(boolean success) {
        this.success = success;
    }

    public AccountResponse(boolean success, AccountDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }

    public AccountResponse(boolean success, Map<String,String> errors) {
        this.success = success;
        this.errors = errors;
    }

    public AccountResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AccountDto getUserDto() {
        return userDto;
    }

    public void setUserDto(AccountDto userDto) {
        this.userDto = userDto;
    }

    public Map<String,String> getError() {
        return errors;
    }

    public void setError(Map<String,String> errors) {
        this.errors = errors;
    }
}
