package pl.financemanagement.User.UserModel;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UsersMapperTest {

    private final static String NAME = "Demo";
    private final static String EMAIL = "demo@financialapp.com";
    private final static String UPDATED_USER_EMAIL = "demo1@financialapp.com";
    private final static String PASSWORD = "password";
    private final static String EXTERNAL_ID = "f9969a5d-55d2-4e31-83e1-5759500a1e6d";

    @Test
    void userDtoMapper() {
        UserDto expected = buildUserDto();

        assertThat(UsersMapper.userDtoMapper(buildUserAccount()))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("externalId")
                .isEqualTo(expected);
    }

    private UserDto buildUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName(NAME);
        userDto.setEmail(EMAIL);
        userDto.setExternalId(EXTERNAL_ID);
        return userDto;
    }

    private static UserAccount buildUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(EMAIL);
        userAccount.setName(NAME);
        userAccount.setPassword(PASSWORD);
        userAccount.setRole("USER");
        userAccount.setExternalId(EXTERNAL_ID);
        return userAccount;
    }
}