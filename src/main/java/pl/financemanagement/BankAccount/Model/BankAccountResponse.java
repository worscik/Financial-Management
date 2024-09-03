package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountResponse {

    private boolean success;
    @JsonProperty("account")
    private BankAccountDto userDto;
    private Map<String,String> errors;

    public BankAccountResponse(boolean success, BankAccountDto userDto, Map<String,String> errors) {
        this.success = success;
        this.userDto = userDto;
        this.errors = errors;
    }

    public BankAccountResponse(boolean success) {
        this.success = success;
    }

    public BankAccountResponse(boolean success, BankAccountDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }

    public BankAccountResponse(boolean success, Map<String,String> errors) {
        this.success = success;
        this.errors = errors;
    }

    public BankAccountResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BankAccountDto getUserDto() {
        return userDto;
    }

    public void setUserDto(BankAccountDto userDto) {
        this.userDto = userDto;
    }

    public Map<String,String> getError() {
        return errors;
    }

    public void setError(Map<String,String> errors) {
        this.errors = errors;
    }
}
