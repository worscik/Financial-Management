package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.financemanagement.User.UserModel.UserDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private boolean success;
    @JsonProperty("account")
    private AccountDto userDto;
    private String error;

}
