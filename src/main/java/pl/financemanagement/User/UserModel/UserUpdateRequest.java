package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;

public class UserUpdateRequest {

    @Email(message = "Email is not correct")
    private String newEmail;
    private String newName;
    private boolean demo;

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
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }
}
