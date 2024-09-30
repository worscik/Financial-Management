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

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
        return new UserResponse(true, userDtoMapper(savedUser), token);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest userRequest, String email) throws JOSEException {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        UserAccount userToUpdate = user.get();

        if (Objects.nonNull(userRequest.getNewEmail())) {
            if (userDao.findUserByEmail(userRequest.getNewEmail()).isPresent()) {
                throw new EmailAlreadyInUseException(userRequest.getNewEmail());
            }
            userToUpdate.setEmail(userRequest.getNewEmail());
        }

        if (Objects.nonNull(userRequest.getNewName())) {
            userToUpdate.setName(userRequest.getNewName());
        }

        userToUpdate.setModifyOn(Instant.now());
        UserAccount savedUser = userDao.save(userToUpdate);
        String token = jwtService.generateUserToken(savedUser.getEmail(), USER.getRole());
        return new UserResponse(true, userDtoMapper(savedUser), token);
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {
        Optional<UserAccount> user = userDao.findUserByEmail(email);
        return user
                .map(userAccount -> new UserResponse(true, userDtoMapper(userAccount)))
                .orElseGet(() -> new UserErrorResponse(false, "User " + email + " does not exists."));
    }

    @Override
    public UserResponse getUserById(long id, String email) throws UserNotFoundException {
        Optional<UserAccount> user = userDao.findById(id);
        if (user.isPresent()) {
            return new UserResponse(true, userDtoMapper(user.get()));
        }
        throw new UserNotFoundException("User with id " + id + " not found");
    }

    @Override
    @Transactional
    public UserDeleteResponse deleteUser(String externalId, String email) throws UserNotFoundException {
        Optional<UserAccount> user = userDao.findUserByEmailAndExternalId(email, UUID.fromString(externalId));
        if (user.isPresent()) {
            userDao.deleteById(user.get().getId());
            return new UserDeleteResponse(true, "User deleted.");
        }
        throw new UserNotFoundException("User with email " + email + " not found");
    }
}
