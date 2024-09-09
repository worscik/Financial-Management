package pl.financemanagement.ApplicationConfig.JWToken;

public enum JWTokenStatus {

    SUCCESS("success"),
    ERROR("error");

    private final String status;

    JWTokenStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
