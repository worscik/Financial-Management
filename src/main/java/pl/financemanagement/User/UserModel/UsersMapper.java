package pl.financemanagement.User.UserModel;

import java.time.Instant;
import java.util.UUID;

public class UsersMapper {

    public static UserAccount userMapper(UserRequest userRequest) {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(userRequest.getEmail());
        userAccount.setName(userRequest.getName());
        return userAccount;
    }

    public static UserDto UserDtoMapper(UserAccount userAccount) {
        UserDto userDto = new UserDto();
        userDto.setEmail(userAccount.getEmail());
        userDto.setName(userAccount.getName());
        userDto.setExternalId(userAccount.getExternalId());
        return userDto;
    }

}
