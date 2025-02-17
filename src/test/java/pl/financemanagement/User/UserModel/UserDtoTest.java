package pl.financemanagement.User.UserModel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UserDtoTest {

    private final static String USER_EMAIL = "demo@financialapp.com";
    private final static String USER_NAME = "Demo";
    private final static String EXTERNAL_ID = "f9969a5d-55d2-4e31-83e1-5759500a1e6d";

    @Test
    void buildUserDto() {
        UserDto expected = createUserDto();

        assertThat(createUserDto())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setExternalId(EXTERNAL_ID);
        userDto.setEmail(USER_EMAIL);
        userDto.setName(USER_NAME);
        userDto.setUserRole(UserRole.USER);
        return userDto;
    }
}