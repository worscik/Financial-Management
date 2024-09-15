package pl.financemanagement.User.UserController;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserRepository.UserDao;
import pl.financemanagement.User.UserService.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController extends DemoResolver<UserService> {

    private final UserDao userDao;

    public UserController(@Qualifier("userServiceImpl") UserService service,
                          @Qualifier("userServiceDemo") UserService demoService,
                          UserDao userDao) {
        super(service, demoService);
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
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserUpdateRequest userRequest,
                                            BindingResult result,
                                            Principal principal) throws JOSEException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(userRequest.isDemo()).updateUser(userRequest, principal.getName()));
    }

    @GetMapping("/email")
    ResponseEntity<UserResponse> checkIfUserExists(@RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                   Principal principal) {
        return ResponseEntity.ok(resolveService(isDemo).isUserExistByEmail(principal.getName()));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id,
                                                    @RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                    Principal principal) {
        Optional<UserAccount> user = userDao.findUserByEmail(principal.getName());
        return user
                .map(userAccount -> ResponseEntity.ok(resolveService(isDemo).getUserById(userAccount.getId())))
                .orElseGet(() ->
                        ResponseEntity.badRequest().body(new UserErrorResponse(false, "User not found")));
    }

    @DeleteMapping("")
    ResponseEntity<UserDeleteResponse> deleteUser(@RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                  Principal principal) {
        Optional<UserAccount> user = userDao.findUserByEmail(principal.getName());
        return user
                .map(result -> ResponseEntity.ok(resolveService(isDemo).deleteUser(user.get().getId(), principal.getName())))
                .orElse(ResponseEntity.badRequest().build());
    }

    static UserErrorResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserErrorResponse(false, errors);
    }

}
