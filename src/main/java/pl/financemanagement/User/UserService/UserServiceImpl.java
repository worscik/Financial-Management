package pl.financemanagement.User.UserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;

@Service
@Qualifier("normalUserService")
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse createUser(UserRequest userRequest) {
//        usersRepository.save()
        return new UserResponse("To be continued", true);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse isUserExistByEmail(String email) {
        return null;
    }

    @Override
    public UserResponse getUserById(long id) {
        return null;
    }

    @Override
    public boolean deleteUser(long id, String email) {
        return false;
    }
}
