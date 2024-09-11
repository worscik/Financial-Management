package pl.financemanagement.User.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserRepository.UserDao;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.User.UserModel.UsersMapper.UserDtoMapper;
import static pl.financemanagement.User.UserModel.UsersMapper.userMapper;

@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final int ONE = 1;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        Optional<UserAccount> userExistByEmail = userDao.findUserByEmail(userRequest.getEmail());
        if (userExistByEmail.isPresent()) {
            log.info("Cannot add user with email: {} because user exists", userRequest.getEmail());
            return new UserErrorResponse(false, "User " + userRequest.getEmail() + " does exists.");
        }
        UserAccount userToSave = userMapper(userRequest);
        userToSave.setCreatedOn(Instant.now());
        userToSave.setExternalId(UUID.randomUUID());
        UserAccount savedUser = userDao.save(userToSave);
        return new UserResponse(true, UserDtoMapper(savedUser));
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userRequest) {
        Optional<UserAccount> userAccount = userDao.findUserByEmail(userRequest.getEmail());
        if (userAccount.isPresent()) {
            UserAccount userToSave = userMapper(userRequest);
            userToSave.setVersion(userToSave.getVersion() + ONE);
            if (!userRequest.getNewEmail().isBlank()) {
                userToSave.setEmail(userRequest.getNewEmail());
            }
            if (!userRequest.getNewName().isBlank()) {
                userToSave.setName(userRequest.getNewName());
            }
            UserAccount savedUser = userDao.save(userToSave);
            return new UserResponse(true, UserDtoMapper(savedUser));
        }
        return new UserErrorResponse(false, "User not found: " + userRequest.getEmail());
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        return user
                .map(userAccount -> new UserResponse(true, UserDtoMapper(userAccount)))
                .orElseGet(() -> new UserErrorResponse(false, "User " + email + " does not exists."));
    }

    @Override
    public UserResponse getUserById(long id) {
        Optional<UserAccount> user = userDao.findById(id);
        return user
                .map(userAccount -> new UserResponse(true, UserDtoMapper(userAccount)))
                .orElseGet(() -> new UserErrorResponse(false, "User do not exists."));
    }

    @Override
    public UserDeleteResponse deleteUser(long id, String email) {
            userDao.deleteById(id);
            return new UserDeleteResponse(true, "User deleted.");
    }
}
