package pl.financemanagement.User.UserController;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.ApplicationConfig.JWToken.JwtService;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserRepository.UserDao;
import pl.financemanagement.User.UserService.UserService;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController extends DemoResolver<UserService> {

    private final JwtService jwtService;
    private final UserDao userDao;

    public UserController(@Qualifier("userServiceImpl") UserService service,
                          @Qualifier("userServiceDemo") UserService demoService, JwtService jwtService, UserDao userDao) {
        super(service, demoService);
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @PostMapping("/register")
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest,
                                            BindingResult result) throws JOSEException {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new UserErrorResponse(false, errors));
        }
        UserResponse response = resolveService(userRequest.isDemo()).createUser(userRequest);
        if (response.isSuccess()) {
            response.setToken(jwtService.generateUserToken(userRequest.getEmail()));
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserUpdateRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(userRequest.isDemo()).updateUser(userRequest));
    }

    @GetMapping("/email/{userEmail}")
    ResponseEntity<UserResponse> isUserExist(@PathVariable String userEmail,
                                             @RequestParam(required = false, defaultValue = "false") boolean isDemo) {
        if (AppTools.isBlank(userEmail)) {
            return ResponseEntity.badRequest().body(new UserErrorResponse(false, "Email cannot be empty"));
        }
        return ResponseEntity.ok(resolveService(isDemo).isUserExistByEmail(userEmail));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id,
                                                    @RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                    Principal principal) {
        Optional<UserAccount> user = userDao.findUserByEmail(principal.getName());
        return user
                .map(userAccount -> ResponseEntity.ok(resolveService(isDemo).getUserById(userAccount.getId())))
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(new UserErrorResponse(false, "User not found")));
    }

    @DeleteMapping("/{id}/{email}")
    ResponseEntity<Boolean> deleteUser(@PathVariable long id,
                                       @PathVariable String email,
                                       @RequestParam(required = false, defaultValue = "false") boolean isDemo) {
        if (AppTools.isBlank(String.valueOf(id))) {
            return ResponseEntity.created(URI.create("localhost:8080")).build();
        }
        UserService userService = resolveService(isDemo);
        return ResponseEntity.ok(userService.deleteUser(id, email));
    }

    static UserErrorResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserErrorResponse(false, errors);
    }

}
