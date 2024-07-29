package pl.financemanagement.User.UserController;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.User.UserModel.UserDto;
import pl.financemanagement.User.UserModel.UserRequest;
import pl.financemanagement.User.UserModel.UserResponse;
import pl.financemanagement.User.UserModel.UserResponseError;
import pl.financemanagement.User.UserService.UserServiceImpl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return null;
    }

    @PutMapping
    ResponseEntity<UserResponse> upsertUser(@RequestBody @Valid UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return null;
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        if(AppTools.isBlank(String.valueOf(id))){
            return ResponseEntity.badRequest().body(new UserResponse("Id cannot be empty", false));
        }
        return null;
    }

    @GetMapping("/{userEmail}")
    ResponseEntity<UserResponse> isUserExist(@PathVariable String userEmail) {
        if (AppTools.isBlank(userEmail)) {
            return ResponseEntity.badRequest().body(new UserResponse("Email cannot be empty", false));
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        if(AppTools.isBlank(String.valueOf(id))){
            return ResponseEntity.badRequest().body(new UserResponse("Id cannot be empty", false));
        }
        return null;
    }

    @DeleteMapping("/{id}/{email}")
    ResponseEntity<Boolean> deleteUser(@PathVariable long id, @PathVariable String email) {
        if(AppTools.isBlank(String.valueOf(id))){
            return ResponseEntity.created(URI.create("localhost:8080")).build();
        }
        return ResponseEntity.ok().build();
    }

    static UserResponseError buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new UserResponseError(false, null, errors);
    }

}
