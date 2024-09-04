package pl.financemanagement.User.UserService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserDto;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;

import java.util.Optional;

@Service("userService")
public interface UserService {

    Optional<UserResponse> createUser(UserRequest userRequest);

    UserResponse updateUser(UserRequest userRequest);

    UserResponse isUserExistByEmail(String email);

    UserResponse getUserById(long id);

    boolean deleteUser(long id, String email);

}
