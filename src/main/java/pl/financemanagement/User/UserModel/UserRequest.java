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

    @Override
    public String toString() {
        return "UserRequest{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
