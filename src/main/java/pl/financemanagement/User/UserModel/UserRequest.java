package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Email cannot be blank or empty")
    @Email(message = "Email is not correct")
    private String email;
    @NotBlank
    @Size(max = 64)
    private String name;
    @NotBlank(message = "Password cannot be blank or empty")
    @Size(min = 1)
    private String password;

    public UserRequest() {
    }

    public UserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
