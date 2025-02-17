package pl.financemanagement.User.UserModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

}
