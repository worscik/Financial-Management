package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserModel.exceptions.EmailAlreadyInUseException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;

@Service("userService")
public interface UserService {

    UserResponse createUser(UserRequest userRequest) throws JOSEException;

    UserResponse updateUser(UserUpdateRequest userRequest, String email) throws JOSEException;

    UserResponse isUserExistByEmail(String email);

    UserResponse getUserById(long id, String email);

    UserDeleteResponse deleteUser(String externalId, String email);

}
