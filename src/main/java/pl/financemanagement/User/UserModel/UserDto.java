package pl.financemanagement.User.UserModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
