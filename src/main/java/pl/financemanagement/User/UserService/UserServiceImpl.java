package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.JWToken.Service.JwtService;
import pl.financemanagement.PasswordTools.PasswordService;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserModel.exceptions.EmailAlreadyInUseException;
import pl.financemanagement.User.UserModel.exceptions.UserExistsException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserDao;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.User.UserModel.UserRole.USER;
import static pl.financemanagement.User.UserModel.UsersMapper.userDtoMapper;

@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final static String USER_ROLE = "USER";

    private final JwtService jwtService;
    private final UserDao userDao;
    private final PasswordService passwordService;

    public UserServiceImpl(JwtService jwtService, UserDao userDao, PasswordService passwordService) {
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.passwordService = passwordService;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) throws JOSEException {
        LOGGER.info("Attempting to create user with email: {}", userRequest.getEmail());
        Optional<UserAccount> existingUser = findUserByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
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
        UserAccount userAccount = userDao.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " does not exist"));

        if (AppTools.isNotBlank(userRequest.getNewEmail())
                && !isEmailAvailable(userRequest.getNewEmail(), userAccount)) {
            throw new EmailAlreadyInUseException("Email " + userRequest.getNewEmail() + " is already in use.");
        }

        if (userRequest.getNewEmail() != null) {
            userAccount.setEmail(userRequest.getNewEmail());
        }
        if (userRequest.getNewName() != null) {
            userAccount.setName(userRequest.getNewName());
        }
        userAccount.setModifyOn(Instant.now());
        UserAccount savedUser = userDao.saveUserAccount(userAccount);
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

    private Optional<UserAccount> findUserByEmail(String email) {
        Optional<UserAccount> account = userDao.findUserByEmail(email);
        return account;
    }

    private boolean isEmailAvailable(String newEmail, UserAccount currentAccount) {
        Optional<UserAccount> userWithSameEmail = userDao.findUserByEmail(newEmail);
        return userWithSameEmail.isEmpty() || userWithSameEmail.get().getId() == currentAccount.getId();
    }

    private UserAccount userMapper(UserRequest userRequest) {
        UserAccount userToSave = new UserAccount();
        userToSave.setEmail(userRequest.getEmail());
        userToSave.setName(userRequest.getName());
        userToSave.setCreatedOn(Instant.now());
        userToSave.setExternalId(UUID.randomUUID());
        userToSave.setRole(USER_ROLE);
        userToSave.setPassword(passwordService.hashPassword(userRequest.getPassword()));
        return userToSave;
    }

}
