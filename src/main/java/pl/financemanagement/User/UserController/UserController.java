package pl.financemanagement.User.UserController;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.User.UserModel.*;
import pl.financemanagement.User.UserModel.exceptions.UserExistsException;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserService.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
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
        return ResponseEntity.ok(isDemo(false).createUser(userRequest));
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserUpdateRequest userRequest,
                                            BindingResult result,
                                            Principal principal) throws JOSEException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(principal.getName()).updateUser(userRequest, principal.getName()));
    }

    @GetMapping()
    ResponseEntity<UserResponse> getBasicData(Principal principal) {
        return ResponseEntity.ok(resolveService(principal.getName()).getBasicDataByEmail(principal.getName()));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id,
                                                    Principal principal) throws UserNotFoundException {
        return ResponseEntity.ok(resolveService(principal.getName()).getUserById(id, principal.getName()));
    }

    @DeleteMapping("/{externalId}")
    ResponseEntity<UserDeleteResponse> deleteUser(@RequestParam String externalId,
                                                  Principal principal) throws UserNotFoundException {
        return ResponseEntity.ok(resolveService(principal.getName()).deleteUser(externalId, principal.getName()));
    }

    private UserErrorResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserErrorResponse(false, errors);
    }

}
