package pl.financemanagement.User.UserModel;

import java.time.Instant;
import java.util.UUID;

public class UsersMapper {

    private final static String USER_ROLE = "USER";

    public static UserAccount userMapper(UserRequest userRequest) {
        UserAccount userToSave = new UserAccount();
        userToSave.setEmail(userRequest.getEmail());
        userToSave.setName(userRequest.getName());
        userToSave.setCreatedOn(Instant.now());
        userToSave.setExternalId(UUID.randomUUID());
        userToSave.setRole(USER_ROLE);
        return userToSave;
    }

    public static UserDto userDtoMapper(UserAccount userAccount) {
        UserDto userDto = new UserDto();
        userDto.setEmail(userAccount.getEmail());
        userDto.setName(userAccount.getName());
        userDto.setExternalId(userAccount.getExternalId());
        return userDto;
    }

}
