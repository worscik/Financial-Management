package pl.financemanagement.User.UserModel;

public enum UserRole {

    USER("user"),
    DEMO("demo"),
    ADMIN("admin");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
