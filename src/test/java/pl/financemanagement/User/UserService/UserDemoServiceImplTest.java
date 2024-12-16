package pl.financemanagement.User.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.financemanagement.User.UserModel.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserDemoServiceImplTest {

    private final static String USER_EMAIL = "demo@financialapp.com";
    private final static String USER_NAME = "demo";
    private final static String USER_ROLE = "USER";
    private final static String USER_PASSWORD = "passwd123";
    private final static String UPDATED_USER_EMAIL = "demo1@example.com";
    private final static String FAKE_USER_EMAIL = "demoooo@example.com";
    private final static long USER_LONG_ID = 2L;
    private final static String UUID = "f9969a5d-55d2-4e31-83e1-5759500a1e6d";


    @InjectMocks
    UserDemoServiceImpl userDemoService;

    @Test
    void createUser() {
        UserResponse userResponse = new UserResponse(true, buildUserDto());

        assertThat(userDemoService.createUser(buildUserRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponse);
    }

    @Test
    void updateUser() {
        UserResponse userResponse = new UserResponse(true, buildUserDtoWithUpdatedEmail());

        assertThat(userDemoService.updateUser(buildUserUpdateRequest(), USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponse);
    }

    @Test
    void getBasicDataByEmailWhenContactExists() {
        UserResponse userResponse = new UserResponse(true, buildUserDto());

        assertThat(userDemoService.getBasicDataByEmail(USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponse);
    }

    @Test
    void getBasicDataByEmailWhenUserNotExists() {
        UserErrorResponse userErrorResponse = new UserErrorResponse(false, "User not found");

        assertThat(userDemoService.getBasicDataByEmail(FAKE_USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userErrorResponse);
    }

    @Test
    void getUserByIdWhenContactExists() {
        UserResponse userResponse = new UserResponse(true, buildUserDto());

        assertThat(userDemoService.getUserById(USER_LONG_ID, USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponse);
    }

    @Test
    void getUserByIdWhenContactNotExists() {
        UserErrorResponse userErrorResponse = new UserErrorResponse(false, "User not found");

        assertThat(userDemoService.getUserById(3L, USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userErrorResponse);
    }

    @Test
    void deleteUser() {
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse(true, "User deleted.");

        assertThat(userDemoService.deleteUser(UUID, USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userDeleteResponse);
    }

    private UserRequest buildUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(USER_EMAIL);
        userRequest.setName(USER_EMAIL);
        userRequest.setPassword(USER_PASSWORD);
        return userRequest;
    }

    private UserUpdateRequest buildUserUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setDemo(false);
        userUpdateRequest.setNewEmail(UPDATED_USER_EMAIL);
        userUpdateRequest.setNewName(USER_NAME);
        return userUpdateRequest;
    }


    private static UserDto buildUserDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail(USER_EMAIL);
        userDto.setExternalId(java.util.UUID.fromString(UUID));
        userDto.setName(USER_NAME);
        return userDto;
    }

    private static UserDto buildUserDtoWithUpdatedEmail() {
        UserDto userDto = new UserDto();
        userDto.setName(USER_NAME);
        userDto.setEmail(UPDATED_USER_EMAIL);
        userDto.setExternalId(java.util.UUID.fromString(UUID));
        return userDto;
    }


}