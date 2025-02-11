package pl.financemanagement.User.UserModel;

public class UserDto {

    private String email;
    private String name;
    private String externalId;
    private UserRole userRole;

    private UserDto(String email, String name, String externalId, UserRole userRole) {
        this.email = email;
        this.name = name;
        this.externalId = externalId;
        this.userRole = userRole;
    }

    public static UserDto buildUserDto(String email, String name, String externalId, UserRole userRole) {
        return new UserDto(email, name, externalId, userRole);
    }

    public UserDto() {
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
