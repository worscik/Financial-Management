package pl.financemanagement.JWToken.Model;

import lombok.Getter;

@Getter
public enum JWTokenStatus {

    SUCCESS("success"),
    ERROR("error");

    private final String status;

    JWTokenStatus(String status) {
        this.status = status;
    }

}
