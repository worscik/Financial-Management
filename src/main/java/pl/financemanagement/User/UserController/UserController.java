package pl.financemanagement.User.UserController;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.xml.sax.helpers.DefaultHandler;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserModel.UserResponseError;
import pl.financemanagement.User.UserService.UserService;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends DemoResolver<UserService> {

    @Autowired
    public UserController(@Qualifier("normalUserService") UserService service,
                          @Qualifier("demoUserService") UserService demoService) {
        super(service, demoService);
    }

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        UserService userService = resolveService(userRequest.isDemo());
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(userRequest.isDemo()).updateUser(userRequest));
    }

    //TODO
//    @PatchMapping("/{id}")
//    ResponseEntity<UserResponse> upsertUser(@PathVariable Long id,
//                                            @RequestBody JsonMergePatch patch,
//                                            @RequestBody boolean isSample) {
//        if (AppTools.isBlank(String.valueOf(id))) {
//            return ResponseEntity.badRequest().body(new UserResponse("Id cannot be empty", false));
//        }
//        return ResponseEntity.ok(resolveService(isSample).updateUser(null));
//    }

    @GetMapping("/{userEmail}")
    ResponseEntity<UserResponse> isUserExist(@PathVariable String userEmail,
                                             @RequestBody boolean isSample) {
        if (AppTools.isBlank(userEmail)) {
            return ResponseEntity.badRequest().body(new UserResponse("Email cannot be empty", false));
        }
        return ResponseEntity.ok(resolveService(isSample).isUserExistByEmail(userEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id,
                                                    @RequestBody boolean isSample) {
        if (AppTools.isBlank(String.valueOf(id))) {
            return ResponseEntity.badRequest().body(new UserResponse("Id cannot be empty", false));
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

    static UserResponseError buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserResponseError(false, null, errors);
    }

}
