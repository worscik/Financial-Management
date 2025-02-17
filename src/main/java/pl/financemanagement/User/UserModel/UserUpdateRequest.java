package pl.financemanagement.User.UserModel;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    @Email(message = "Email is not correct")
    private String newEmail;
    private String newName;

}
