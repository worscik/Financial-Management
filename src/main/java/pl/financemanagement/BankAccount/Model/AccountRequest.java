package pl.financemanagement.BankAccount.Model;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class AccountRequest {

    private String externalId;
    private String accountName;
    private String accountNumber;
    private boolean isSample;

    public AccountRequest(String externalId, String accountName, String accountNumber, boolean isSample) {
        this.externalId = externalId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.isSample = isSample;
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

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isSample() {
        return isSample;
    }

    public void setSample(boolean sample) {
        isSample = sample;
    }
}
