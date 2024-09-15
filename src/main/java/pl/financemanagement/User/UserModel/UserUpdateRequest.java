package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;

public class UserUpdateRequest {

    @Email(message = "Email is not correct")
    private String newEmail;
    private String newName;
    private boolean isDemo;

    public UserUpdateRequest() {
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(@Email(message = "Email is not correct") String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }
}
