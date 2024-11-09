package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.JWToken.Service.JwtService;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserModel.exceptions.EmailAlreadyInUseException;
import pl.financemanagement.User.UserModel.exceptions.UserExistsException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.User.UserModel.UserRole.USER;
import static pl.financemanagement.User.UserModel.UsersMapper.userDtoMapper;
import static pl.financemanagement.User.UserModel.UsersMapper.userMapper;

@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final JwtService jwtService;
    private final UserDao userDao;

    public UserServiceImpl(JwtService jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) throws JOSEException {
        LOGGER.info("Attempting to create user with email: {}", userRequest.getEmail());
        UserAccount user = getExistingUserAccountByEmail(userRequest.getEmail());
        if (Objects.nonNull(user)) {
            throw new UserExistsException("User with email " + userRequest.getEmail() + " exists");
        }
        UserAccount userToSave = userMapper(userRequest);
        UserAccount savedUser = userDao.saveUserAccount(userToSave);
        String token = jwtService.generateUserToken(userRequest.getEmail(), USER.getRole());
        return new UserResponse(true, userDtoMapper(savedUser), token);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest userRequest, String email) throws JOSEException {
        UserAccount user = getExistingUserAccountByEmail(email);

        if (Objects.nonNull(userRequest.getNewEmail())) {
            if (userDao.findUserByEmail(userRequest.getNewEmail()).isPresent()) {
                throw new EmailAlreadyInUseException(userRequest.getNewEmail());
            }
            user.setEmail(userRequest.getNewEmail());
        }

        if (Objects.nonNull(userRequest.getNewName())) {
            user.setName(userRequest.getNewName());
        }

        user.setModifyOn(Instant.now());
        UserAccount savedUser = userDao.saveUserAccount(user);
        String token = jwtService.generateUserToken(savedUser.getEmail(), USER.getRole());
        return new UserResponse(true, userDtoMapper(savedUser), token);
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        return user
                .map(userAccount -> new UserResponse(true, userDtoMapper(userAccount)))
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }

    @Override
    public UserResponse getUserById(long id, String email) throws UserNotFoundException {
        Optional<UserAccount> user = userDao.findUserById(id);
        if (user.isPresent()) {
            return new UserResponse(true, userDtoMapper(user.get()));
        }
        throw new UserNotFoundException("User with id " + id + " not found.");
    }

    @Override
    @Transactional
    public UserDeleteResponse deleteUser(String externalId, String email) throws UserNotFoundException {
        Optional<UserAccount> user = userDao.findUserByEmailAndExternalId(email, UUID.fromString(externalId));
        if (user.isPresent()) {
            userDao.deleteUserAccountById(user.get().getId());
            return new UserDeleteResponse(true, "User deleted.");
        }
        throw new UserNotFoundException("User with email " + email + " not found.");
    }

    private UserAccount getExistingUserAccountByEmail(String email) {
        UserAccount account = userDao.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " doesn't exist."));
        LOGGER.info("User with email {} not found.", email);
        return account;
    }
}
