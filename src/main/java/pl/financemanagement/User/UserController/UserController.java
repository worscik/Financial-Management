package pl.financemanagement.User.UserController;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserService.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends DemoResolver<UserService> {

    public UserController(@Qualifier("userServiceImpl") UserService service,
                          @Qualifier("userServiceDemo") UserService demoService) {
        super(service, demoService);
    }

    @PostMapping("/register")
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest,
                                            BindingResult result) throws JOSEException, UserExistsException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        UserResponse response = resolveService(userRequest.isDemo()).createUser(userRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response);
        }
        throw new UserExistsException("User with email " + userRequest.getEmail() + " exists");
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserUpdateRequest userRequest,
                                            BindingResult result,
                                            Principal principal) throws JOSEException, UserNotFoundException, EmailAlreadyInUseException {
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
                                                    Principal principal) throws UserNotFoundException {
        return ResponseEntity.ok(resolveService(isDemo).getUserById(id, principal.getName()));
    }

    @DeleteMapping("/{externalId}")
    ResponseEntity<UserDeleteResponse> deleteUser(@RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                  @RequestParam String externalId,
                                                  Principal principal) throws UserNotFoundException {
        return ResponseEntity.ok(resolveService(isDemo).deleteUser(externalId, principal.getName()));
    }

    static UserErrorResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserErrorResponse(false, errors);
    }

}
