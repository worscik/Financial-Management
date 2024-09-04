package pl.financemanagement.User.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.User;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserRepository.UsersRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static pl.financemanagement.User.UserModel.UsersMapper.UserDtoMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final static UUID RANDOM_UUID = UUID.randomUUID();
    private final static Instant NOW = Instant.now();

    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserWhenNotExist() {
        UserResponse expected = new UserResponse(true, UserDtoMapper(buildUserAccount()));
        when(usersRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        when(usersRepository.save(any())).thenReturn(buildUserAccount());
        assertThat(userService.createUser(buildUserRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void createUserWhenExist() {
        UserResponse expected = new UserResponse("User does exist", false);
        when(usersRepository.findUserByEmail(any())).thenReturn(Optional.of(buildUserAccount()));
        assertThat(userService.createUser(buildUserRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void createUserException() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");

        when(usersRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(usersRepository.save(any(UserAccount.class))).thenThrow(new RuntimeException("Database error"));

        UserResponse response = userService.createUser(userRequest);

        assertFalse(response.isSuccess());
        assertEquals("Error while user was adding", response.getError()  );
    }

    @Test
    void updateUserWhenExists() {
        UserResponse expected = new UserResponse(true, buildUserDto());
        when(usersRepository.findUserByEmail(any())).thenReturn(Optional.of(buildUserAccount()));
        when(usersRepository.save(any())).thenReturn(buildUserAccount());
        assertThat(userService.updateUser(buildUserUpdateRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void updateUserWhenNotExists() {
        UserResponse expected = new UserResponse("User not found: " + "example1@user.pl", false);
        when(usersRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        assertThat(userService.updateUser(buildUserUpdateRequest()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void updateUserException() {
        when(usersRepository.findUserByEmail(anyString())).thenThrow(new NullPointerException("Mocked Exception"));
        UserResponse response = userService.updateUser(buildUserUpdateRequest());
        assertEquals("Error when user be updated", response.getError());
        assertFalse(response.isSuccess());

    }

    @Test
    void isUserExistByEmailWhenIsExists() {
        UserResponse expected = new UserResponse(true, UserDtoMapper(buildUserAccount()));
        when(usersRepository.findUserByEmail("test email")).thenReturn(Optional.of(buildUserAccount()));
        assertThat(userService.isUserExistByEmail("test email"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void isUserExistByEmailWhenNotExists() {
        UserResponse expected = new UserResponse("User " + "test email" + " does not exists.", false);
        when(usersRepository.findUserByEmail("test email")).thenReturn(Optional.empty());
        assertThat(userService.isUserExistByEmail("test email"))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void getUserByIdWhenExists() {
        UserResponse expected = new UserResponse(true, UserDtoMapper(buildUserAccount()));
        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(buildUserAccount()));
        assertThat(userService.getUserById(1L))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void getUserByIdWhenNotExists() {
        UserResponse expected = new UserResponse("User do not exists.", false);
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThat(userService.getUserById(1L))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void deleteUserWhenExists() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(buildUserAccount()));
        assertThat(userService.deleteUser(1L, "example@email.com"))
                .isNotNull()
                .isTrue();
    }

    @Test
    void deleteUserWhenNotExists() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThat(userService.deleteUser(1L, "example@email.com"))
                .isNotNull()
                .isFalse();
    }

    private UserAccount buildUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setName("Test User");
        userAccount.setVersion(1);
        userAccount.setExternalId(RANDOM_UUID);
        userAccount.setId(1L);
        userAccount.setEmail("example@user.pl");
        userAccount.setModifyOn(NOW);
        userAccount.setCreatedOn(NOW);
        return userAccount;
    }

    private UserRequest buildUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("example1@user.pl");
        userRequest.setDemo(false);
        return userRequest;
    }

    private UserUpdateRequest buildUserUpdateRequest() {
        UserUpdateRequest userRequest = new UserUpdateRequest();
        userRequest.setNewName("Test User1");
        userRequest.setNewEmail("example123@user.pl");
        userRequest.setEmail("example1@user.pl");
        userRequest.setDemo(false);
        return userRequest;
    }

    private UserDto buildUserDto() {
        UserDto userDto = new UserDto();
        userDto.setExternalId(RANDOM_UUID);
        userDto.setName("Test User");
        userDto.setEmail("example@user.pl");
        return userDto;
    }

}