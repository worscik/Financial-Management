package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;

public class UserUpdateRequest extends UserRequest {

    @Email(message = "Email is not correct")
    private String newEmail;
    private String newName;

    public UserUpdateRequest(String newEmail, String newName) {
        this.newEmail = newEmail;
        this.newName = newName;
    }

    public UserUpdateRequest(String email, String name, String password, boolean isDemo, String newEmail, String newName) {
        super(email, name, password, isDemo);
        this.newEmail = newEmail;
        this.newName = newName;
    }

    public UserUpdateRequest() {
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
