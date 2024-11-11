package pl.financemanagement.User.UserModel;

public class UsersMapper {

    public static UserDto userDtoMapper(UserAccount userAccount) {
        UserDto userDto = new UserDto();
        userDto.setEmail(userAccount.getEmail());
        userDto.setName(userAccount.getName());
        userDto.setExternalId(userAccount.getExternalId());
        return userDto;
    }

}
