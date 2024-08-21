package pl.financemanagement.User.UserModel;

import java.util.UUID;

public class UserDto {

    private String email;
    private String name;
    private UUID externalId;

    public UserDto(String email, String name, UUID externalId) {
        this.email = email;
        this.name = name;
        this.externalId = externalId;
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

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }
}
