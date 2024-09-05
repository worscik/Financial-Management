package pl.financemanagement.User.UserController;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.User.UserModel.UserErrorResponse;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserModel.UserUpdateRequest;
import pl.financemanagement.User.UserService.UserService;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends DemoResolver<UserService> {

    public UserController(@Qualifier("userServiceImpl") UserService service,
                          @Qualifier("userServiceDemo") UserService demoService) {
        super(service, demoService);
    }

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest,
                                            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new UserErrorResponse(false,errors));
        }
        UserService userService = resolveService(userRequest.isDemo());
        UserResponse response = userService.createUser(userRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserUpdateRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(userRequest.isDemo()).updateUser(userRequest));
    }

    @GetMapping("/{userEmail}")
    ResponseEntity<UserResponse> isUserExist(@PathVariable String userEmail,
                                             @RequestBody boolean isSample) {
        if (AppTools.isBlank(userEmail)) {
            return ResponseEntity.badRequest().body(new UserErrorResponse(false, "Email cannot be empty"));
        }
        return ResponseEntity.ok(resolveService(isSample).isUserExistByEmail(userEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id,
                                                    @RequestBody boolean isSample) {
        if (AppTools.isBlank(String.valueOf(id))) {
            return ResponseEntity.badRequest().body(new UserErrorResponse(false, "Id cannot be empty"));
        }
        return ResponseEntity.ok(resolveService(isSample).getUserById(id));
    }

    @DeleteMapping("/{id}/{email}")
    ResponseEntity<Boolean> deleteUser(@PathVariable long id,
                                       @PathVariable String email,
                                       @RequestBody boolean isSample) {
        if (AppTools.isBlank(String.valueOf(id))) {
            return ResponseEntity.created(URI.create("localhost:8080")).build();
        }
        UserService userService = resolveService(isSample);
        return ResponseEntity.ok(userService.deleteUser(id, email));
    }

    static UserErrorResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserErrorResponse(false, errors);
    }

}
