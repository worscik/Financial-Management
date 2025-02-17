package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.financemanagement.JWToken.Service.JwtService;
import pl.financemanagement.PasswordTools.PasswordServiceImpl;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserModel.exceptions.EmailAlreadyInUseException;
import pl.financemanagement.User.UserModel.exceptions.UserExistsException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserAccountRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.financemanagement.User.UserModel.UsersMapper.userDtoMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final String TOKEN = "04ccbd93-150f-43cd-b54f-a5e92d7c4a9a";
    private final String EXTERNAL_ID = "04ccbd93-150f-43cd-b15f-a5e92d7c4a9a";
    private final String USER_EMAIL = "example@email.pl";
    private final String USER_UPDATED_EMAIL = "trele@morele.pl";
    private final String TEST_USER_EMAIL = "test@example.pl";
    private final String NAME = "Name";
    private final String PASSWORD = "passwd";

    @Mock
    private JwtService jwtService;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PasswordServiceImpl passwordServiceImpl;

    @InjectMocks
    private UserServiceImpl userService;

    @ParameterizedTest
    @ValueSource(strings = {"exampleemail@test.com", "exampleemail1@test.com", "exampleemai22@test.com"})
    void createUserWhenNotExists(String email) throws JOSEException {
        when(userAccountRepository.save(any())).thenReturn(buildUserAccount(PASSWORD, NAME, email));
        when(jwtService.generateUserToken(any(), any())).thenReturn(TOKEN);

        UserResponse userResponse =
                new UserResponse(true, UsersMapper.userDtoMapper(buildUserAccount(PASSWORD, NAME, email)), TOKEN);

        assertThat(userService.createUser(buildUserRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponse);
    }

    @Test
    void createUserWhenUserAlreadyExists() {
        UserRequest userRequest = buildUserRequest();
        UserAccount existingUser = buildUserAccount(PASSWORD, NAME, "existingUser@example.com");
        when(userAccountRepository.findUserByEmail(userRequest.getEmail())).thenReturn(Optional.of(existingUser));

        UserExistsException exception = assertThrows(UserExistsException.class, () -> {
            userService.createUser(userRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("User with email " + userRequest.getEmail() + " exists");
        verify(userAccountRepository, never()).save(any());
    }

    @Test
    void updateUserWhenUserExists() throws JOSEException {
        when(userAccountRepository.findUserByEmail(any()))
                .thenReturn(Optional.of(buildUserAccount(PASSWORD, NAME, USER_EMAIL)));
        when(userAccountRepository.save(any())).thenReturn(buildUserAccount(PASSWORD, NAME, USER_EMAIL));
        when(jwtService.generateUserToken(any(), any())).thenReturn(TOKEN);

        UserResponse expectedResponse =
                new UserResponse(true, userDtoMapper(buildUserAccount(PASSWORD, NAME, USER_EMAIL)), TOKEN);

        assertThat(userService.updateUser(buildUserUpdateRequest(), USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);

    }

    @Test
    void updateUserWhenUserExistsAndIsNewEmail() throws JOSEException {
        UserAccount userAccount = buildUserAccount(PASSWORD, NAME, USER_EMAIL);
        userAccount.setEmail(USER_UPDATED_EMAIL);

        when(userAccountRepository.findUserByEmail(any()))
                .thenReturn(Optional.of(buildUserAccount(PASSWORD, NAME, USER_EMAIL)));
        when(userAccountRepository.save(any())).thenReturn(userAccount);
        when(jwtService.generateUserToken(any(), any())).thenReturn(TOKEN);

        UserResponse expectedResponse = new UserResponse(true, userDtoMapper(userAccount),
                TOKEN);

        assertThat(userService.updateUser(buildUserUpdateRequest(), "example1@email.pl"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void updateUserWhenUserExistsEmailIsUsed() throws JOSEException {
        UserAccount userAccount = buildUserAccount(PASSWORD, NAME, USER_EMAIL);
        userAccount.setEmail(USER_UPDATED_EMAIL);
        UserUpdateRequest userAccount1 = buildUserUpdateRequest();
        userAccount1.setNewEmail(USER_UPDATED_EMAIL);

        when(userAccountRepository.findUserByEmail(any())).thenReturn(Optional.of(userAccount));

        EmailAlreadyInUseException exception = assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.updateUser(userAccount1, TEST_USER_EMAIL);
        });

        assertThat(exception.getMessage()).isEqualTo("Email " + userAccount.getEmail() + " is already in use.");
        verify(userAccountRepository, never()).save(any());

    }

    @Test
    void isUserExistByEmail() {
        UserAccount userAccount = buildUserAccount(PASSWORD, NAME, USER_EMAIL);
        UserResponse expectedResponse = new UserResponse(true, userDtoMapper(userAccount));

        when(userAccountRepository.findUserByEmail(any()))
                .thenReturn(Optional.of(buildUserAccount(PASSWORD, NAME, USER_EMAIL)));

        assertThat(userService.getBasicDataByEmail(TEST_USER_EMAIL))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void isUserNotExistByEmail() {
        when(userAccountRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getBasicDataByEmail("email");
        });
    }

    @Test
    void getUserByIdWhenExists() {
        UserAccount userAccount = buildUserAccount(PASSWORD, NAME, USER_EMAIL);
        UserResponse expectedResponse = new UserResponse(true, userDtoMapper(userAccount));

        when(userAccountRepository.findUserById(anyLong()))
                .thenReturn(Optional.of(buildUserAccount(PASSWORD, NAME, USER_EMAIL)));

        assertThat(userService.getUserById(1L, "email"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void getUserByIdWhenNotExists() {
        when(userAccountRepository.findUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1, "email");
        });
    }

    @Test
    void deleteUserWhenExists() {
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse(
                true, "User deleted.");

        when(userAccountRepository.findUserByEmailAndExternalId("deletedEmail", EXTERNAL_ID))
                .thenReturn(Optional.of(buildUserAccount(PASSWORD, NAME, USER_EMAIL)));

        assertThat(userService.deleteUser(EXTERNAL_ID, "deletedEmail"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userDeleteResponse);
    }

    @Test
    void deleteUserWhenNotExists() {
        when(userAccountRepository.findUserByEmailAndExternalId("deletedEmail", EXTERNAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(EXTERNAL_ID, "deletedEmail");
        });
    }

    private UserRequest buildUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test");
        userRequest.setPassword("1234");
        userRequest.setEmail(TEST_USER_EMAIL);
        return userRequest;
    }

    private UserAccount buildUserAccount(String password, String name, String email) {
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword(password);
        userAccount.setName(name);
        userAccount.setEmail(email);
        userAccount.setExternalId(EXTERNAL_ID);
        return userAccount;
    }

    private UserUpdateRequest buildUserUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setNewEmail("");
        userUpdateRequest.setNewName("");
        return userUpdateRequest;
    }

    private UserResponse buildUserResponse() {
        return new UserResponse(true, UsersMapper.userDtoMapper(buildUserAccount(PASSWORD, NAME, USER_EMAIL)));
    }

}