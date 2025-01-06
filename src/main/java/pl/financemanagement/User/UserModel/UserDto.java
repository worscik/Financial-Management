package pl.financemanagement.User.UserModel;

import java.util.UUID;

public class UserDto {

    private String email;
    private String name;
    private String externalId;

    private UserDto(String email, String name, String externalId) {
        this.email = email;
        this.name = name;
        this.externalId = externalId;
    }

    public static UserDto buildUserDto(String email, String name, String externalId) {
        return new UserDto(email, name, externalId);
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
}
