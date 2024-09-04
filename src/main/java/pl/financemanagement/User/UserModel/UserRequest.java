package pl.financemanagement.User.UserModel;

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
    private boolean isDemo = false;

    public UserRequest() {
    }

    public UserRequest(String email, String name, boolean isDemo) {
        this.email = email;
        this.name = name;
        this.isDemo = isDemo;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }
}
