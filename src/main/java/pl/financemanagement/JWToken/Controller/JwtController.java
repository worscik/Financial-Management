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
import pl.financemanagement.PasswordTools.PasswordService;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserCredentialsRequest;
import pl.financemanagement.User.UserRepository.UserAccountRepository;

import java.util.HashMap;
import java.util.Map;

import static pl.financemanagement.JWToken.Model.JWTokenStatus.SUCCESS;

@RestController
@CrossOrigin(value = "*")
public class JwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtController.class);

    private final JwtServiceImpl jwtService;
    private final UserAccountRepository userAccountRepository;
    private final PasswordService passwordService;

    public JwtController(JwtServiceImpl jwtService,
                         UserAccountRepository userAccountRepository,
                         PasswordService passwordService) {
        this.jwtService = jwtService;
        this.userAccountRepository = userAccountRepository;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTokenResponse> generateToken(@Valid @RequestBody UserCredentialsRequest request,
                                                         BindingResult result) throws JOSEException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        UserAccount user = userAccountRepository.findUserByEmail(request.getEmail())
                .stream()
                .filter(userAccount -> passwordService.verifyPassword(request.getPassword(), userAccount.getPassword()))
                .findFirst()
                .orElseThrow(() -> new ForbiddenAccessException("Wrong email or password. Try again!"));

        LOGGER.info("Correctly authorized user: {}. Generating new token.", request.getEmail());

        String token = jwtService.generateUserToken(request.getEmail(), user.getUserRole().getRole());

        return ResponseEntity.ok().body(new JWTokenResponse(token, null, SUCCESS.getStatus(), user.getUserRole()));

    }

    static JWTokenResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new JWTokenResponse(errors, JWTokenStatus.ERROR.getStatus());
    }
}
