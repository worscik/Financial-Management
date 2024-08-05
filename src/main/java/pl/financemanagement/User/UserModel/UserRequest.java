package pl.financemanagement.User.UserModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(max = 64)
    private String name;
    @JsonProperty("isDemo")
    private boolean isDemo = false;

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(max = 64) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 64) String name) {
        this.name = name;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }
}
