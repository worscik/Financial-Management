package pl.financemanagement.User.UserService;

import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserDto;
import pl.financemanagement.User.UserModel.UserRequest;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto createUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return false;
    }

    @Override
    public UserDto getUserById(long id) {
        return null;
    }

    @Override
    public boolean deleteUser(long id, String email) {
        return false;
    }
}
