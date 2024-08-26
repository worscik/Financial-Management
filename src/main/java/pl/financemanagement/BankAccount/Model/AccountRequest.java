package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class AccountRequest {

    private String externalId;
    @NotBlank
    private String accountName;
    @JsonProperty("isDemo")
    private boolean isDemo = false;

    public AccountRequest(String externalId, String accountName, boolean isDemo) {
        this.externalId = externalId;
        this.accountName = accountName;
        this.isDemo = isDemo;
    }

    public AccountRequest(String accountName) {
        this.accountName = accountName;
    }

    public AccountRequest() {
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getAccountName() {
        return accountName;
    }


    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }
}
