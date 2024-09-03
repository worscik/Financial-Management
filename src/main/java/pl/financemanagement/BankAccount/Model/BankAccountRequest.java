package pl.financemanagement.BankAccount.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class BankAccountRequest {

    private String externalId;
    @NotBlank
    private String accountName;
    private long userId;
    @JsonProperty("isDemo")
    private boolean isDemo = false;

    public BankAccountRequest(String externalId, String accountName, long userId, boolean isDemo) {
        this.externalId = externalId;
        this.accountName = accountName;
        this.userId = userId;
        this.isDemo = isDemo;
    }

    public BankAccountRequest(String accountName, long userId) {
        this.accountName = accountName;
        this.userId = userId;
    }

    public BankAccountRequest(long userId) {
        this.userId = userId;
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

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }
}
