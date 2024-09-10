package pl.financemanagement.ApplicationConfig.JWToken;


import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserCredentialsRequest;
import pl.financemanagement.User.UserRepository.UserDao;

import java.security.Principal;
import java.util.Optional;

import static pl.financemanagement.ApplicationConfig.JWToken.JWTokenStatus.ERROR;
import static pl.financemanagement.ApplicationConfig.JWToken.JWTokenStatus.SUCCESS;

@RestController
@CrossOrigin(value = "*")
public class JwtController {

    private static final String USER = "user";
    private final JwtServiceImpl jwtService;
    private final UserDao userDao;

    public JwtController(JwtServiceImpl jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTokenResponse> getToken(@RequestBody UserCredentialsRequest userCredentialsRequest,
                                                    Principal principal)
            throws JOSEException {
        Optional<UserAccount> user = userDao.findUserByEmail(principal.getName());
        if(user.isPresent()) {
            return ResponseEntity.ok().body(new JWTokenResponse(
                    jwtService.generateUserToken(userCredentialsRequest.login()), "", SUCCESS.getStatus()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JWTokenResponse("",
                "Incorrect login information sent", ERROR.getStatus()));
    }

}
