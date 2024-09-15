package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.UserDeleteResponse;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserModel.UserUpdateRequest;

import java.security.Principal;

@Service("userService")
public interface UserService {

    UserResponse createUser(UserRequest userRequest) throws JOSEException;

    UserResponse updateUser(UserUpdateRequest userRequest, String email) throws JOSEException;

    UserResponse isUserExistByEmail(String email);

    UserResponse getUserById(long id);

    UserDeleteResponse deleteUser(long id, String email);

}
