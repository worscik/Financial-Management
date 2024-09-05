package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountResponse {

    private boolean success;
    @JsonProperty("account")
    private BankAccountDto userDto;

    public BankAccountResponse(boolean success, BankAccountDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }

    public BankAccountResponse(boolean success) {
        this.success = success;
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

}
