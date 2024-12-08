package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.financemanagement.JWToken.Service.JwtService;
import pl.financemanagement.PasswordTools.PasswordService;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserModel.exceptions.EmailAlreadyInUseException;
import pl.financemanagement.User.UserModel.exceptions.UserExistsException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.financemanagement.User.UserModel.UsersMapper.userDtoMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final String TOKEN = "04ccbd93-150f-43cd-b54f-a5e92d7c4a9a";
    private final String EXTERNAL_ID = "04ccbd93-150f-43cd-b15f-a5e92d7c4a9a";

    @Mock
    private JwtService jwtService;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void createUserWhenNotExists() throws JOSEException {
        when(userDao.saveUserAccount(any())).thenReturn(buildUserAccount());
        when(jwtService.generateUserToken(any(), any())).thenReturn(TOKEN);
        UserResponse userResponse = new UserResponse(true, UsersMapper.userDtoMapper(buildUserAccount()), TOKEN);

        assertThat(userService.createUser(buildUserRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponse);
    }

    @Test
    void createUserWhenUserAlreadyExists() throws JOSEException {
        UserRequest userRequest = buildUserRequest();
        UserAccount existingUser = buildUserAccount();
        when(userDao.findUserByEmail(userRequest.getEmail())).thenReturn(Optional.of(existingUser));

        UserExistsException exception = assertThrows(UserExistsException.class, () -> {
            userService.createUser(userRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("User with email " + userRequest.getEmail() + " exists");

        verify(userDao, never()).saveUserAccount(any());

    }

    @Test
    void updateUserWhenUserExists() throws JOSEException {
        when(userDao.findUserByEmail(any())).thenReturn(Optional.of(buildUserAccount()));
        when(userDao.saveUserAccount(any())).thenReturn(buildUserAccount());
        when(jwtService.generateUserToken(any(), any())).thenReturn(TOKEN);

        UserResponse expectedResponse = new UserResponse(true, userDtoMapper(buildUserAccount()), TOKEN);

        assertThat(userService.updateUser(buildUserUpdateRequest(), "example@email.pl"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);

    }

    @Test
    void updateUserWhenUserExistsAndIsNewEmail() throws JOSEException {
        UserAccount userAccount = buildUserAccount();
        userAccount.setEmail("trele@morele.pl");

        when(userDao.findUserByEmail(any())).thenReturn(Optional.of(buildUserAccount()));
        when(userDao.saveUserAccount(any())).thenReturn(userAccount);
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
        UserAccount userAccount = buildUserAccount();
        userAccount.setEmail("trele@morele.pl");
        UserUpdateRequest userAccount1 = buildUserUpdateRequest();
        userAccount1.setNewEmail("trele@morele.pl");

        when(userDao.findUserByEmail(any())).thenReturn(Optional.of(userAccount));

        EmailAlreadyInUseException exception = assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.updateUser(userAccount1, "test@example.pl");
        });

        assertThat(exception.getMessage()).isEqualTo("Email " + userAccount.getEmail() + " is already in use.");

        verify(userDao, never()).saveUserAccount(any());

    }

    @Test
    void isUserExistByEmail() {
        UserAccount userAccount = buildUserAccount();
        UserResponse expectedResponse = new UserResponse(true, userDtoMapper(userAccount));

        when(userDao.findUserByEmail(any())).thenReturn(Optional.of(buildUserAccount()));

        assertThat(userService.getBasicDataByEmail("test@example.pl"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void isUserNotExistByEmail() {
        when(userDao.findUserByEmail(any())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getBasicDataByEmail("email");
        });
    }

    @Test
    void getUserByIdWhenExists() {
        UserAccount userAccount = buildUserAccount();
        UserResponse expectedResponse = new UserResponse(true, userDtoMapper(userAccount));

        when(userDao.findUserById(anyLong())).thenReturn(Optional.of(buildUserAccount()));

        assertThat(userService.getUserById(1L, "email"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void getUserByIdWhenNotExists() {
        when(userDao.findUserById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1, "email");
        });
    }

    @Test
    void deleteUserWhenExists() {
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse(
                true, "User deleted.");

        when(userDao.findUserByEmailAndExternalId("deletedEmail", UUID.fromString(EXTERNAL_ID)))
                .thenReturn(Optional.of(buildUserAccount()));

        assertThat(userService.deleteUser(EXTERNAL_ID, "deletedEmail"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userDeleteResponse);
    }

    @Test
    void deleteUserWhenNotExists() {
        when(userDao.findUserByEmailAndExternalId("deletedEmail", UUID.fromString(EXTERNAL_ID)))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
           userService.deleteUser(EXTERNAL_ID, "deletedEmail");
        });
    }

    private UserRequest buildUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test");
        userRequest.setPassword("1234");
        userRequest.setEmail("test@example.pl");
        return userRequest;
    }

    private UserAccount buildUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword("1234");
        userAccount.setName("Test");
        userAccount.setEmail("test@example.pl");
        userAccount.setExternalId(EXTERNAL_ID);
        return userAccount;
    }

    private UserUpdateRequest buildUserUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setDemo(false);
        userUpdateRequest.setNewEmail("");
        userUpdateRequest.setNewName("");
        return userUpdateRequest;
    }

    private UserResponse buildUserResponse() {
        return new UserResponse(true, UsersMapper.userDtoMapper(buildUserAccount()));
    }

}