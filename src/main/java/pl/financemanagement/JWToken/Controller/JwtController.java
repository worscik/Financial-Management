package pl.financemanagement.JWToken.Controller;


import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.JWToken.Model.JWTokenResponse;
import pl.financemanagement.JWToken.Model.JWTokenStatus;
import pl.financemanagement.JWToken.Model.exceptions.ForbiddenAccessException;
import pl.financemanagement.JWToken.Service.JwtServiceImpl;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserCredentialsRequest;
import pl.financemanagement.User.UserRepository.UserDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static pl.financemanagement.JWToken.Model.JWTokenStatus.SUCCESS;

@RestController
@CrossOrigin(value = "*")
public class JwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtController.class);

    private final JwtServiceImpl jwtService;
    private final UserDao userDao;

    public JwtController(JwtServiceImpl jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTokenResponse> generateToken(@Valid @RequestBody UserCredentialsRequest userCredentialsRequest,
                                                         BindingResult result) throws JOSEException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        Optional<UserAccount> user = userDao.findUserByCredentials(userCredentialsRequest);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(new JWTokenResponse(jwtService.generateUserToken(
                    userCredentialsRequest.getEmail(), user.get().getRole()), null, SUCCESS.getStatus()));
        }
        LOGGER.info("Correctly authorized user: {}", userCredentialsRequest.getEmail());
        throw new ForbiddenAccessException("Cannot authorize credentials");
    }

    static JWTokenResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new JWTokenResponse(errors, JWTokenStatus.ERROR.getStatus());
    }
}
