package pl.financemanagement.User.UserModel;

import java.util.UUID;

public class UsersMapper {

    public static UserDto userDtoMapper(UserAccount userAccount) {
       return UserDto.buildUserDto(userAccount.getEmail(), userAccount.getName(),
               UUID.fromString(userAccount.getExternalId()));
    }

}
