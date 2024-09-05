package pl.financemanagement.User.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserRepository.UserDao;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.User.UserModel.UsersMapper.UserDtoMapper;
import static pl.financemanagement.User.UserModel.UsersMapper.userMapper;

@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

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
            return new UserErrorResponse(false, "User does exist");
        }
        try {
            UserAccount userToSave = userMapper(userRequest);
            userToSave.setCreatedOn(Instant.now());
            userToSave.setExternalId(UUID.randomUUID());
            UserAccount savedUser = userDao.save(userToSave);
            return new UserResponse(true, UserDtoMapper(savedUser));
        } catch (Exception e) {
            log.error("Error while user was adding with email: {}", userRequest.getEmail(), e);
            return new UserErrorResponse(false, "Error while user was adding");
        }
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userRequest) {
        try {
            Optional<UserAccount> userAccountOptional = userDao.findUserByEmail(userRequest.getEmail());

            if (userAccountOptional.isPresent()) {
                UserAccount userToSave = userAccountOptional.get();

                if (Objects.nonNull(userRequest.getNewEmail()) && !userRequest.getNewEmail().isBlank()) {
                    userToSave.setEmail(userRequest.getNewEmail());
                }

                if (Objects.nonNull(userRequest.getNewName()) && !userRequest.getNewName().isBlank()) {
                    userToSave.setName(userRequest.getNewName());
                }

                UserAccount savedUser = userDao.save(userToSave);
                return new UserResponse(true, UserDtoMapper(savedUser));
            } else {
                return new UserErrorResponse(false, "User not found: " + userRequest.getEmail());
            }
        } catch (Exception e) {
            log.error("Error when user be updated with email: {}", userRequest.getEmail(), e);
            return new UserErrorResponse(false, "Error when user be updated");
        }
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
    public boolean deleteUser(long id, String email) {
        Optional<UserAccount> user = userDao.findById(id);
        if (user.isPresent()) {
            userDao.deleteById(id);
            return true;
        }
        log.info("Cannot delete user. User with id {} was not found", id);
        return false;
    }
}
