package pl.financemanagement.JWToken.Controller;


import com.nimbusds.jose.JOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.JWToken.Model.JWTokenResponse;
import pl.financemanagement.JWToken.Service.JwtServiceImpl;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserCredentialsRequest;
import pl.financemanagement.User.UserRepository.UserDao;

import java.util.Optional;

import static pl.financemanagement.JWToken.Model.JWTokenStatus.ERROR;
import static pl.financemanagement.JWToken.Model.JWTokenStatus.SUCCESS;

@RestController
@CrossOrigin(value = "*")
public class JwtController {

    private final JwtServiceImpl jwtService;
    private final UserDao userDao;

    public JwtController(JwtServiceImpl jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTokenResponse> generateToken(@RequestBody UserCredentialsRequest userCredentialsRequest)
            throws JOSEException {
        Optional<UserAccount> user = userDao.findUserByEmail(userCredentialsRequest.getLogin());
        if(user.isPresent()) {
            return ResponseEntity.ok().body(new JWTokenResponse(jwtService.generateUserToken(
                            userCredentialsRequest.getLogin(), user.get().getRole()), null, SUCCESS.getStatus()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JWTokenResponse(null,
                "Incorrect login information sent", ERROR.getStatus()));
    }

}
