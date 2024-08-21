package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.financemanagement.User.UserModel.UserDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private boolean success;
    @JsonProperty("account")
    private AccountDto userDto;
    private String error;

    public AccountResponse(boolean success, AccountDto userDto, String error) {
        this.success = success;
        this.userDto = userDto;
        this.error = error;
    }

    public AccountResponse(boolean success, AccountDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }

    public AccountResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
