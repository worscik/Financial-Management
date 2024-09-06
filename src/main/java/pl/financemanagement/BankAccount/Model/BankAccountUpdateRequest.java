package pl.financemanagement.BankAccount.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BankAccountUpdateRequest {

    @NotBlank
    @Size(min = 5, max = 64)
    private String accountName;

    public BankAccountUpdateRequest(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
