package pl.financemanagement.User.UserModel;

public class UsersMapper {

    public static UserAccount userMapper(UserRequest userRequest) {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(userRequest.getEmail());
        userAccount.setName(userRequest.getName());
        userAccount.setRole("USER");
        return userAccount;
    }

    public static UserDto userDtoMapper(UserAccount userAccount) {
        UserDto userDto = new UserDto();
        userDto.setEmail(userAccount.getEmail());
        userDto.setName(userAccount.getName());
        userDto.setExternalId(userAccount.getExternalId());
        return userDto;
    }

}
