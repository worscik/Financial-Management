package pl.financemanagement.User.UserService;

import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;
import pl.financemanagement.User.UserModel.*;

import java.security.Principal;

@Service("userService")
public interface UserService {

    UserResponse createUser(UserRequest userRequest) throws JOSEException;

    UserResponse updateUser(UserUpdateRequest userRequest, String email) throws JOSEException, UserNotFoundException, EmailAlreadyInUseException;

    UserResponse isUserExistByEmail(String email);

    UserResponse getUserById(long id, String email) throws UserNotFoundException;

    UserDeleteResponse deleteUser(String externalId, String email) throws UserNotFoundException;

}
