package pl.financemanagement.User.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserRepository.UsersRepository;

import java.util.Optional;

import static pl.financemanagement.User.UserModel.UsersMapper.UserDtoMapper;
import static pl.financemanagement.User.UserModel.UsersMapper.userMapper;

@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Optional<UserResponse> createUser(UserRequest userRequest) {
        Optional<UserAccount> userExistByEmail = usersRepository.findUserByEmail(userRequest.getEmail());
        if (userExistByEmail.isPresent()) {
            log.info("Cannot add user with email: {} because user exists", userRequest.getEmail());
            return Optional.empty();
        }
        try {
            UserAccount userToSave = userMapper(userRequest);
            UserAccount savedUser = usersRepository.save(userToSave);
            return Optional.of(new UserResponse(true, UserDtoMapper(savedUser)));
        } catch (Exception e) {
            log.error("Error while user was adding with email: {}", userRequest.getEmail(), e);
            return Optional.of(new UserResponse("Error while user was adding", false));
        }
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        try {
            Optional<UserAccount> userAccount = usersRepository.findUserByEmail(userRequest.getEmail());
            if (userAccount.isPresent()) {
                UserAccount userToSave = userMapper(userRequest);
                UserAccount savedUser = usersRepository.save(userToSave);
                return new UserResponse(true, UserDtoMapper(savedUser));
            }
            return new UserResponse("User not found: " + userRequest.getEmail(), false);
        } catch (Exception e) {
            log.error("Error when user be updated with email: {}", userRequest.getEmail(), e);
            return new UserResponse("Error when user be updated", false);
        }
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {

            Optional<UserAccount> user = usersRepository.findUserByEmail(email);
            return user
                    .map(userAccount -> new UserResponse(true, UserDtoMapper(userAccount)))
                    .orElseGet(() -> new UserResponse("User " + email + " does not exists.", false));
    }

    @Override
    public UserResponse getUserById(long id) {
            Optional<UserAccount> user = usersRepository.findById(id);
            return user
                    .map(userAccount -> new UserResponse(true, UserDtoMapper(userAccount)))
                    .orElseGet(() -> new UserResponse("User do not exists.", false));
    }

    @Override
    public boolean deleteUser(long id, String email) {
        Optional<UserAccount> user = usersRepository.findById(id);
        if (user.isPresent()) {
            usersRepository.deleteById(id);
            return true;
        }
        log.info("Cannot delete user. User with id {} was not found", id);
        return false;
    }
}
