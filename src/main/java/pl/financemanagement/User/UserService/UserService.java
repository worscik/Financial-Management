package pl.financemanagement.User.UserService;

import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserDto;
import pl.financemanagement.User.UserModel.UserRequest;

@Service("userService")
public interface UserService {

    UserDto createUser(UserRequest userRequest);

    UserDto updateUser(UserRequest userRequest);

    boolean isUserExistByEmail(String email);

    UserDto getUserById(long id);

    boolean deleteUser(long id, String email);

}
