package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;

public class UserUpdateRequest extends UserRequest {

    @Email
    private String newEmail;
    private String newName;

    public UserUpdateRequest(String email, String name, boolean isDemo, String newEmail, String newName) {
        super(email, name, isDemo);
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