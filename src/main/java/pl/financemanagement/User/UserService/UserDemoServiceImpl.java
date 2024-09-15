package pl.financemanagement.User.UserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.*;

import java.util.UUID;

@Service
@Qualifier("userServiceDemo")
public class UserDemoServiceImpl implements UserService {

    private final static String USER_EMAIL = "demo@example.com";
    private final static String UPDATED_USER_EMAIL = "demo1@example.com";
    private final static UUID EXTERNAL_ID = UUID.fromString("f9969a5d-55d2-4e31-83e1-5759500a1e6d");

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        UserDto userDto = new UserDto("demo@example.com", USER_EMAIL, EXTERNAL_ID);
        return new UserResponse(true, userDto);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userRequest, String email) {
        UserDto userDto = new UserDto("demo@example.com", UPDATED_USER_EMAIL, EXTERNAL_ID);
        return new UserResponse(true, userDto);
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {
        if(USER_EMAIL.equals(email)){
            UserDto userDto = new UserDto("demo@example.com", USER_EMAIL, EXTERNAL_ID);
            return new UserResponse(true, userDto);
        }
        return new UserErrorResponse(false, "User not found");
    }

    @Override
    public UserResponse getUserById(long id) {
        if (id == 1) {
            UserDto userDto = new UserDto("demo@example.com", USER_EMAIL, EXTERNAL_ID);
            return new UserResponse(true, userDto);
        }
        return new UserErrorResponse(false, "User not found");
    }

    @Override
    public UserDeleteResponse deleteUser(long id, String email) {
        return new UserDeleteResponse(true, "User deleted.");
    }
}
