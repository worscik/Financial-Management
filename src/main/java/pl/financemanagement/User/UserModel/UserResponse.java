package pl.financemanagement.User.UserModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private boolean success;
    @JsonProperty("user")
    private UserDto userDto;

    public UserResponse(boolean success, UserDto userDto) {
        this.success = success;
        this.userDto = userDto;
    }

    public UserResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

}
