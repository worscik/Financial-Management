package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;

public class UserUpdateRequest {

    @Email(message = "Email is not correct")
    private String newEmail;
    private String newName;

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
