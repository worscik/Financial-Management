package pl.financemanagement.User.UserModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private boolean success;
    @JsonProperty("user")
    private UserDto userDto;
    private String token;

    public UserResponse(boolean success, UserDto userDto, String token) {
        this.success = success;
        this.userDto = userDto;
        this.token = token;
    }

    public UserResponse(boolean success, UserDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }

    public UserResponse(boolean success) {
        this.success = success;
    }

}
