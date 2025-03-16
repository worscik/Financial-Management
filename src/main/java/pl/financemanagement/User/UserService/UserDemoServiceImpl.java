package pl.financemanagement.User.UserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.*;

import java.util.UUID;

import static pl.financemanagement.User.UserModel.UserRole.USER;

@Service
@Qualifier("userServiceDemo")
public class UserDemoServiceImpl implements UserService {

    private final static String USER_EMAIL = "demo@financialapp.com";
    private final static String UPDATED_USER_EMAIL = "demo1@financialapp.com";
    private final static String USER_NAME = "Demo";
    private final static UUID EXTERNAL_ID = UUID.fromString("f9969a5d-55d2-4e31-83e1-5759500a1e6d");

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        return new UserResponse(true, buildUserDto());
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userRequest, String email) {
        UserDto userDto = UserDto.buildUserDto(UPDATED_USER_EMAIL, USER_NAME, EXTERNAL_ID, USER);
        return new UserResponse(true, userDto);
    }

    @Override
    public UserResponse getBasicData(String email) {
        return new UserResponse(true, buildUserDto());
    }

    @Override
    public UserResponse getUserById(long id, String email) {
        return new UserResponse(true, buildUserDto());
    }

    @Override
    public UserDeleteResponse deleteUser(UUID externalId, String email) {
        return new UserDeleteResponse(true, "User deleted.");
    }

    private UserDto buildUserDto() {
        return UserDto.buildUserDto(USER_EMAIL, USER_NAME, EXTERNAL_ID, USER);
    }

}
