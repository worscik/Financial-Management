package pl.financemanagement.BankAccount.Model;

import jakarta.validation.constraints.NotBlank;

public class BankAccountFindRequest {

   @NotBlank
   private String externalId;
   private boolean isDemo = false;

    public BankAccountFindRequest(String externalId, boolean isDemo) {
        this.externalId = externalId;
        this.isDemo = isDemo;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }
}
