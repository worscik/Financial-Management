package pl.financemanagement.User.UserService;

import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserModel.UserUpdateRequest;

@Service("userService")
public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(UserUpdateRequest userRequest);

    UserResponse isUserExistByEmail(String email);

    UserResponse getUserById(long id);

    boolean deleteUser(long id, String email);

}
