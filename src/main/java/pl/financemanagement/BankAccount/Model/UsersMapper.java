package pl.financemanagement.BankAccount.Model;

import pl.financemanagement.User.UserModel.User;
import pl.financemanagement.User.UserModel.UserDto;

import java.time.Instant;

public class UsersMapper {

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(user.getName());
        user.setModifyOn(Instant.now());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }

}
