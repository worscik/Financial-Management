package pl.financemanagement.User.UserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserDto;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;

@Service
@Qualifier("demoUserService")
public class UserDemoServiceImpl implements UserService {

    private final static String USER_EMAIL = "exampleUserName";
    private final static String UPDATED_USER_EMAIL = "exampleUserNameUpdated";

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        UserDto userDto = new UserDto(1L, "example@domain.com", USER_EMAIL);
        return new UserResponse(true, userDto);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        UserDto userDto = new UserDto(1L, "example@domain.com", UPDATED_USER_EMAIL);
        return new UserResponse(true, userDto);
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {
        if(USER_EMAIL.equals(email)){
            UserDto userDto = new UserDto(1L, "example@domain.com", USER_EMAIL);
            return new UserResponse(true, userDto);
        }
        return new UserResponse("User not found", false);
    }

    @Override
    public UserResponse getUserById(long id) {
        if (id == 1) {
            UserDto userDto = new UserDto(1L, "example@domain.com", USER_EMAIL);
            return new UserResponse(true, userDto);
        }
        return new UserResponse("User not found", false);
    }

    @Override
    public boolean deleteUser(long id, String email) {
        return true;
    }
}
