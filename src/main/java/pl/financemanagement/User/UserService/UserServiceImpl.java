package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.JWToken.Service.JwtService;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserRepository.UserDao;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static pl.financemanagement.User.UserModel.UserRole.USER;
import static pl.financemanagement.User.UserModel.UsersMapper.UserDtoMapper;
import static pl.financemanagement.User.UserModel.UsersMapper.userMapper;

@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String USER_ROLE = "USER";

    private final JwtService jwtService;
    private final UserDao userDao;

    public UserServiceImpl(JwtService jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) throws JOSEException {
        Optional<UserAccount> userExistByEmail = userDao.findUserByEmail(userRequest.getEmail());
        if (userExistByEmail.isPresent()) {
            log.info("Cannot add user with email: {} because user exists", userRequest.getEmail());
            return new UserErrorResponse(false, "User " + userRequest.getEmail() + " does exists.");
        }
        UserAccount userToSave = userMapper(userRequest);
        userToSave.setCreatedOn(Instant.now());
        userToSave.setExternalId(UUID.randomUUID());
        UserAccount savedUser = userDao.save(userToSave);
        String token = jwtService.generateUserToken(userRequest.getEmail(), USER.getRole());
        return new UserResponse(true, UserDtoMapper(savedUser), token);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userRequest, String email) throws JOSEException {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        if (user.isPresent()) {
            UserAccount userToUpdate = user.get();

            if (Objects.nonNull(userRequest.getNewEmail())) {
                if (userDao.findUserByEmail(userRequest.getNewEmail()).isPresent()) {
                    return new UserErrorResponse(false, "Email already in use: " + userRequest.getNewEmail());
                }
                userToUpdate.setEmail(userRequest.getNewEmail());
            }
            if (Objects.nonNull(userRequest.getNewName())) {
                userToUpdate.setName(userRequest.getNewName());
            }
            userToUpdate.setModifyOn(Instant.now());
            userToUpdate.setVersion(userToUpdate.getVersion() + 1);
            UserAccount savedUser = userDao.save(userToUpdate);
            String token = jwtService.generateUserToken(savedUser.getEmail(), USER_ROLE);
            return new UserResponse(true, UserDtoMapper(savedUser), token);
        }
        return new UserErrorResponse(false, "User not found: " + email);
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
